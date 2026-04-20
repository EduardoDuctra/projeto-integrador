package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarValorMaximoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoFinanceiraDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoFinanceiraService {


    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;



    public TransacaoFinanceiraService(TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {

        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }


    /**
     * usar somente para CRÉDITO
     * @param dto
     * @return
     */
    @Transactional
    public TransacaoFinanceiraDTO criarTransacao(Long idUsuario, TransacaoFinanceiraDTO dto) {

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

        return new TransacaoFinanceiraDTO(
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




    public List<TransacaoFinanceiraDTO> listarPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoFinanceiraDTO> listaDTO = new ArrayList<>();

        for (Transacao transacaoFinanceira : transacoes) {

            if(transacaoFinanceira.getTipoTransacao().equals(TipoTransacao.CREDITO)) {

                TransacaoFinanceiraDTO dto = new TransacaoFinanceiraDTO(
                        transacaoFinanceira.getValorRecarga(),
                        transacaoFinanceira.getDataInicio()
                );

                listaDTO.add(dto);

            }

        }

        return listaDTO;
    }

}
