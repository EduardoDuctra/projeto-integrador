package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.*;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
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
    private final ConectorRepository conectorRepository;


    public TransacaoFinanceiraService(TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService, TransacaoSubject transacaoSubject, ConectorRepository conectorRepository) {

        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.transacaoSubject = transacaoSubject;
        this.conectorRepository = conectorRepository;
    }


    /**
     * usar somente para CRÉDITO
     * @param dto
     * @return
     */
    @Transactional
    public TransacaoCreditoDTO criarTransacao(Long idUsuario, TransacaoCreditoDTO dto) {

        if (dto.valorRecarga() == null) {
            throw new RegraDeNegociosException("Valor não pode ser nulo");
        }


        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        Transacao transacaoFinanceira = new Transacao();


        transacaoFinanceira.setDataInicio(LocalDateTime.now());


        if(dto.valorRecarga() != null) {
            transacaoFinanceira.setTipoTransacao(TipoTransacao.CREDITO);
            transacaoFinanceira.setStatusTransacao(StatusTransacao.APROVADA);
            transacaoFinanceira.setValorRecarga(dto.valorRecarga());
        }

        transacaoFinanceira.setUsuario(usuario);

        BigDecimal saldoUsuario = usuario.getSaldo().add(dto.valorRecarga());
        usuario.setSaldo(saldoUsuario);


        usuarioRepository.save(usuario);


        Transacao salva = transacaoRepository.save(transacaoFinanceira);

        //observer
        transacaoSubject.notificar(idUsuario);

        return new TransacaoCreditoDTO(
                salva.getValorRecarga(),
                salva.getDataInicio()
        );
    }





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




    public List<TransacaoCreditoDTO> listarPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoCreditoDTO> listaDTO = new ArrayList<>();

        for (Transacao transacaoFinanceira : transacoes) {

            if(transacaoFinanceira.getTipoTransacao().equals(TipoTransacao.CREDITO)) {

                TransacaoCreditoDTO dto = new TransacaoCreditoDTO(
                        transacaoFinanceira.getValorRecarga(),
                        transacaoFinanceira.getDataInicio()
                );

                listaDTO.add(dto);

            }

        }

        return listaDTO;
    }

    public List<TransacaoDebitoDTO> listarTransacaoDebitoPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoDebitoDTO> listaDTO = new ArrayList<>();

        for (Transacao transacaoFinanceira : transacoes) {

            if(transacaoFinanceira.getTipoTransacao().equals(TipoTransacao.DEBITO)) {

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




    public void atualizarSaldoUsuario(Long id, BigDecimal valor) {

        Usuario usuario = usuarioService.buscarPorId(id);

        BigDecimal saldoAtual = usuario.getSaldo();
        BigDecimal novoSaldo = saldoAtual.subtract(valor);


        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            novoSaldo = BigDecimal.ZERO;
        }

        usuario.setSaldo(novoSaldo);
        usuarioRepository.save(usuario);


//        //observer
//        transacaoSubject.notificar(id);

    }
}

