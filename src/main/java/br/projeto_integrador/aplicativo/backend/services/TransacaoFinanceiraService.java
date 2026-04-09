package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoFinanceiraDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.TransacaoFinanceira;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoFinanceiraRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoFinanceiraService {


    private final TransacaoFinanceiraRepository transacaoFinanceiraRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;



    public TransacaoFinanceiraService(TransacaoFinanceiraRepository transacaoFinanceiraRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {

        this.transacaoFinanceiraRepository = transacaoFinanceiraRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }


    /**
     * usar somente para CRÉDITO
     * @param dto
     * @return
     */
    @Transactional
    public TransacaoFinanceiraDTO criarTransacao(TransacaoFinanceiraDTO dto) {

        if (dto.valor() == null) {
            throw new RegraDeNegociosException("Valor não pode ser nulo");
        }

        Usuario usuario = usuarioService.buscarPorId(dto.idUsuario());

        TransacaoFinanceira transacaoFinanceira = new TransacaoFinanceira();


        if (dto.data() != null) {
            transacaoFinanceira.setData(dto.data());
        } else {
            transacaoFinanceira.setData(LocalDateTime.now());
        }

        if (dto.statusTransacao() == null ||
                dto.statusTransacao() != StatusTransacao.REPROVADA && dto.statusTransacao() != StatusTransacao.APROVADA) {

            throw new RegraDeNegociosException("Status incorreto");
        }

        if (dto.tipoTransacao() == null || dto.tipoTransacao() != TipoTransacao.CREDITO) {

            throw new RegraDeNegociosException("Tipo transacao incorreto");

        }

        transacaoFinanceira.setValor(dto.valor());
        transacaoFinanceira.setUsuario(usuario);
        transacaoFinanceira.setTipoTransacao(dto.tipoTransacao());
        transacaoFinanceira.setStatusTransacao(dto.statusTransacao());


        BigDecimal saldoUsuario = usuario.getSaldo().add(dto.valor());
        usuario.setSaldo(saldoUsuario);



        usuarioRepository.save(usuario);


        TransacaoFinanceira salva = transacaoFinanceiraRepository.save(transacaoFinanceira);

        return new TransacaoFinanceiraDTO(
                salva.getIdTransacaoFinanceira(),
                salva.getTipoTransacao(),
                salva.getValor(),
                salva.getStatusTransacao(),
                salva.getData(),
                salva.getUsuario().getIdUsuario(),
                null
        );
    }



    //todo -> atualizar conforme valor gasto
    //todo -> estimar recarga
    @Transactional
    public TransacaoFinanceiraDTO atualizarTransacao(Long id, TransacaoFinanceiraDTO dto) {

        TransacaoFinanceira transacaoRecarga = transacaoFinanceiraRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Transação não encontrada"));

        if (dto.idUsuario() != null) {
            Usuario usuario = usuarioService.buscarPorId(dto.idUsuario());

            transacaoRecarga.setUsuario(usuario);

            //atualizo o saldo depois do fim
            //todo -> verificar se tem saldo suficiente antes de iniciar
            // todo -> alterar status para concluido (ver codigo adicional)
            if (dto.tipoTransacao() == TipoTransacao.CREDITO) {
                BigDecimal saldoUsuario = usuario.getSaldo().add(dto.valor());
                usuario.setSaldo(saldoUsuario);
            } else if (dto.tipoTransacao() == TipoTransacao.DEBITO) {
                BigDecimal saldoUsuario = usuario.getSaldo().subtract(dto.valor());
                usuario.setSaldo(saldoUsuario);
            }
        }

        if (dto.valor() != null) {
            transacaoRecarga.setValor(dto.valor());
        }

        if (dto.tipoTransacao() != null) {
            transacaoRecarga.setTipoTransacao(dto.tipoTransacao());
        }

        if (dto.statusTransacao() != null) {
            transacaoRecarga.setStatusTransacao(dto.statusTransacao());
        }


        if (dto.statusTransacao() == StatusTransacao.CONCLUIDA &&
                transacaoRecarga.getStatusTransacao() != StatusTransacao.CONCLUIDA) {

            transacaoRecarga.setData(LocalDateTime.now());
        }

        TransacaoFinanceira salva = transacaoFinanceiraRepository.save(transacaoRecarga);

        //todo transacao recarga  -> listar tb

        return new TransacaoFinanceiraDTO(
                salva.getIdTransacaoFinanceira(),
                salva.getTipoTransacao(),
                salva.getValor(),
                salva.getStatusTransacao(),
                salva.getData(),
                salva.getUsuario().getIdUsuario(),
                null
        );
    }


    public List<TransacaoFinanceiraDTO> listarPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<TransacaoFinanceira> transacoes = transacaoFinanceiraRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoFinanceiraDTO> listaDTO = new ArrayList<>();

        for (TransacaoFinanceira transacaoFinanceira : transacoes) {

            TransacaoFinanceiraDTO dto = new TransacaoFinanceiraDTO(
                    transacaoFinanceira.getIdTransacaoFinanceira(),
                    transacaoFinanceira.getTipoTransacao(),
                    transacaoFinanceira.getValor(),
                    transacaoFinanceira.getStatusTransacao(),
                    transacaoFinanceira.getData(),
                    transacaoFinanceira.getUsuario().getIdUsuario(),
                    null
            );

            listaDTO.add(dto);
        }

        return listaDTO;
    }

}
