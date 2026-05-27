package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarValorMaximoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoAtivaDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoCreditoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoDebitoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import br.projeto_integrador.aplicativo.backend.websocket.TransacaoSubject;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoFinanceiraService {


    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final TransacaoSubject transacaoSubject;


    public TransacaoFinanceiraService(TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository,
                                      UsuarioService usuarioService, TransacaoSubject transacaoSubject) {

        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.transacaoSubject = transacaoSubject;
    }


    /**
     * usar somente para CRÉDITO
     * crio a transação de crédito, mas primeiro ela fica como PENDENTE
     * notifico o observer
     * @param dto
     * @return TransacaoCreditoDTO
     */
    @Transactional
    public TransacaoCreditoDTO criarTransacao(Long idUsuario, TransacaoCreditoDTO dto) {

        if (dto.valorRecarga() == null) {
            throw new RegraDeNegociosException("Valor não pode ser nulo");
        }

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        Transacao transacaoFinanceira = new Transacao();


        transacaoFinanceira.setDataInicio(LocalDateTime.now());

        //crio a transação de crédito, mas primeiro ela fica como PENDENTE
        if(dto.valorRecarga() != null) {
            transacaoFinanceira.setTipoTransacao(TipoTransacao.CREDITO);
            transacaoFinanceira.setStatusTransacao(StatusTransacao.PENDENTE);
            transacaoFinanceira.setValorRecarga(dto.valorRecarga());
        }

        transacaoFinanceira.setUsuario(usuario);


        Transacao salva = transacaoRepository.save(transacaoFinanceira);

        //observer
        transacaoSubject.notificar(idUsuario);

        return new TransacaoCreditoDTO(
                salva.getValorRecarga(),
                salva.getDataInicio(),
                salva.getId());
    }


    /**
     * usar para atualizar o valor máximo de uma transação -> até quanto quer gastar na recarga
     * @param idUsuario
     * @param idTransacao
     * @param valorMaximo
     * @return AtualizarValorMaximoDTO
     */
    @Transactional
    public AtualizarValorMaximoDTO atualizarTransacao(Long idUsuario, Long idTransacao, BigDecimal valorMaximo) {

        Transacao transacao = transacaoRepository
                .findByIdTransacao(idTransacao)
                .orElseThrow(() -> new RegraDeNegociosException("Transação não encontrada"));


        if (!transacao.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RegraDeNegociosException("Usuário não autorizado");
        }

        if (valorMaximo == null || valorMaximo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegociosException("Valor máximo inválido");
        }

        transacao.setValorMaximo(valorMaximo);


        transacaoRepository.save(transacao);



        return new AtualizarValorMaximoDTO(valorMaximo);
    }


    /**
     * lista as transações de crédito do usuário
     * @param idUsuario
     * @return  List<TransacaoCreditoDTO>
     */
    public List<TransacaoCreditoDTO> listarPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoCreditoDTO> listaDTO = new ArrayList<>();

        for (Transacao transacaoFinanceira : transacoes) {

            if(transacaoFinanceira.getTipoTransacao().equals(TipoTransacao.CREDITO)) {

                if(transacaoFinanceira.getStatusTransacao().equals(StatusTransacao.APROVADA)) {

                    TransacaoCreditoDTO dto = new TransacaoCreditoDTO(
                            transacaoFinanceira.getValorRecarga(),
                            transacaoFinanceira.getDataInicio(),
                            transacaoFinanceira.getId()
                    );

                    listaDTO.add(dto);
                }


            }

        }

        return listaDTO;
    }

    /**
     * lista as transações de débito do usuário
     * @param idUsuario
     * @return  List<TransacaoDebitoDTO>
     */
    public List<TransacaoDebitoDTO> listarTransacaoDebitoPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoDebitoDTO> listaDTO = new ArrayList<>();

        for (Transacao transacaoFinanceira : transacoes) {

            if(transacaoFinanceira.getTipoTransacao().equals(TipoTransacao.DEBITO) &&
                    transacaoFinanceira.getEnergiaConsumida()!=null && transacaoFinanceira.getValorRecarga()!=null
            ) {

                TransacaoDebitoDTO dto = new TransacaoDebitoDTO(
                        transacaoFinanceira.getValorRecarga(),
                        transacaoFinanceira.getEnergiaConsumida(),
                        transacaoFinanceira.getDataInicio(),
                        transacaoFinanceira.getModeloVeiculo()
                );

                listaDTO.add(dto);

            }

        }

        return listaDTO;
    }


    /**
     * lista a transação ativa do usuário -> o carregamento ativo dele
     * @param id
     * @return TransacaoAtivaDTO
     */
    public TransacaoAtivaDTO listarTransacaoAtivaPorUsuario(Long id) {

        Usuario usuario = usuarioService.buscarPorId(id);

        Optional<Transacao> transacaoAtiva = transacaoRepository.findTopByUsuarioAndStatusTransacao(usuario, StatusTransacao.Charging);

        if (transacaoAtiva.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação ativa encontrada para o usuário");
        }

        Transacao t = transacaoAtiva.get();

        return new TransacaoAtivaDTO(
                t.getId(),
                t.getIdTransacao(),
                t.getStatusTransacao(),
                t.getValorRecarga(),
                t.getValorMaximo(),
                t.getDataInicio(),
                t.getSocAtual(),
                t.getConector().getConnectorIdNoCarregador(),
                t.getConector().getCarregador().getIdCarregador()
        );

    }


    /**
     * atualiza o saldo do usuário
     * roda no fim da transação de energia
     * @param id
     * @param valor
     */
    public void atualizarSaldoUsuario(Long id, BigDecimal valor) {

        //lock -> evitar concorrência
        Usuario usuario = usuarioRepository.buscarPorIdComLock(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BigDecimal saldoAtual = usuario.getSaldo();
        BigDecimal novoSaldo = saldoAtual.subtract(valor);


        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            novoSaldo = BigDecimal.ZERO;
        }

        usuario.setSaldo(novoSaldo);
        usuarioRepository.save(usuario);


    }
}

