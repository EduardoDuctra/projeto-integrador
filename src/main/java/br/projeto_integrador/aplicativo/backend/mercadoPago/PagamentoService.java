package br.projeto_integrador.aplicativo.backend.mercadoPago;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import br.projeto_integrador.aplicativo.backend.websocket.TransacaoSubject;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagamentoService {

    @Value("${api.mercado.pago.token}")
    private String accessToken;

    @Value("${ngrok.url}")
    private String webhookUrl;

    private final UsuarioRepository usuarioRepository;
    private final TransacaoRepository transacaoRepository;
    private final TransacaoSubject transacaoSubject;


    public PagamentoService(UsuarioRepository usuarioRepository, TransacaoRepository transacaoRepository,
                            TransacaoSubject transacaoSubject) {
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
        this.transacaoSubject = transacaoSubject;
    }

    /**
     * cria a transação na API do mercado pago
     * retorna um link
     * accessToken -> é da api
     * backurl estão desativadas -> não vou redirecionar
     * webhookUrl usa ngrok pq meu local host não consegue acessar a api publica
     * ngrok cria o tunel para o localhost receber a notificação do mercado pago
     * usando o sandbox -> para produção ajustar
     * @param valor
     * @param idTransacao
     * @return
     * @throws Exception
     */
    public String criarPagamento(Double valor, String idTransacao) throws Exception {

        System.out.println("=== INICIO criarPagamento ===");

        try {
            System.out.println("[TOKEN API MERCADO PAGO]: " + accessToken);

            if (accessToken == null || accessToken.isEmpty()) {
                throw new Exception("AccessToken está NULL ou vazio");
            }

            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceItemRequest item =
                    PreferenceItemRequest.builder()
                            .title("Recarga de crédito")
                            .quantity(1)
                            .currencyId("BRL")
                            .unitPrice(BigDecimal.valueOf(valor))
                            .build();


            PreferenceRequest request =
                    PreferenceRequest.builder()
                            .items(List.of(item))
                            .externalReference(String.valueOf(idTransacao))
                            .notificationUrl(webhookUrl)
//                            .backUrls(
//                                    PreferenceBackUrlsRequest.builder()
//                                            .success("https://google.com")
//                                            .failure("https://google.com")
//                                            .pending("https://google.com")
//                                            .build()
//                            )
//                            .autoReturn("all")
                            .build();



            PreferenceClient client = new PreferenceClient();


            Preference preference = client.create(request);

            System.out.println("=== FIM criarPagamento ===");

            System.out.println("WEBHOOK URL: " + webhookUrl);

            //ajustar no ambiente de produção
            return preference.getSandboxInitPoint();

        } catch (Exception e) {
            System.out.println("=== ERRO AO CRIAR PAGAMENTO ===");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * recebe o webhook
     * se a url dizer approved -> salva como aprovado no bd (atualiza de pendente para APROVADO)
     * notifica o observer para atualizar no front
     * evito a concorrência aqui
     * @param webhookDTO
     * @return String
     * @throws MPException
     * @throws MPApiException
     */
    @Transactional
    public String processarPagamento(WebhookDTO webhookDTO) throws MPException, MPApiException {

        if (webhookDTO.data() == null) {
            return "Sem data";
        }

        String type = webhookDTO.type();
        Long paymentId = webhookDTO.data().id();

        if ("payment".equals(type)) {

            MercadoPagoConfig.setAccessToken(accessToken);

            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);

            String idTransacao = payment.getExternalReference();

            //evita concorrência
            Transacao transacao = transacaoRepository
                    .buscarPorIdComLock(Long.valueOf(idTransacao))
                    .orElseThrow(() -> new RegraDeNegociosException("Erro ao buscar transação no webhook"));

            if ("approved".equals(payment.getStatus())) {

                System.out.println("Pagamento aprovado para transação: " + idTransacao);

                //salvar no banco
                if (transacao.getStatusTransacao() != StatusTransacao.APROVADA) {

                    transacao.setStatusTransacao(StatusTransacao.APROVADA);

                    Usuario usuario = transacao.getUsuario();

                    //atualizar saldo usuario
                    usuario.setSaldo(usuario.getSaldo().add(transacao.getValorRecarga()));

                    usuarioRepository.save(usuario);
                    transacaoRepository.save(transacao);

                    System.out.println("NOTIFICANDO USUARIO " + usuario.getIdUsuario());
                    //observer
                    transacaoSubject.notificarSaldo(usuario.getIdUsuario(), usuario.getSaldo());
                }

            } else {

                System.out.println("Pagamento reprovado para transação: " + idTransacao);


                transacao.setStatusTransacao(StatusTransacao.REPROVADA);
                transacaoRepository.save(transacao);

            }
        }

        return "[ProcessarPagamento - Webhook] - OK";
    }
}