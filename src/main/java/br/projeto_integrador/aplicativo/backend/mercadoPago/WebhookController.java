package br.projeto_integrador.aplicativo.backend.mercadoPago;


import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Value("${api.mercado.pago.token}")
    private String accessToken;



    private final PagamentoService pagamentoService;


    public WebhookController(PagamentoService pagamentoService) {

        this.pagamentoService = pagamentoService;
    }


    /**
     * nessa url que o webhook avisa meu backend
     * tenho que configurar no mercado pago essa url
     * @param webhookDTO
     * @return String
     */
    @PostMapping
    public ResponseEntity<String> receberWebhook(@RequestBody WebhookDTO webhookDTO) {

        System.out.println("WEBHOOK CHEGOU!");

        String confirmacao = null;

        try {

            confirmacao = pagamentoService.processarPagamento(webhookDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(confirmacao);
    }

}
