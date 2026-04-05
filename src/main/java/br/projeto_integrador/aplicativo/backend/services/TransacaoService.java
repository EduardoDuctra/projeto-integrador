package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {


    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;



    public TransacaoService(TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {

        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }


    //listar

    @Transactional
    public TransacaoDTO criarTransacao(TransacaoDTO transacaoDTO) {


        if (transacaoDTO.valor() == null) {
            throw new RegraDeNegociosException("Valor não pode ser nulo");
        }

        Usuario usuario = usuarioService.buscarPorId(transacaoDTO.idUsuario());

        Transacao transacao = new Transacao();


        if(transacaoDTO.data() != null){
            transacao.setData(transacaoDTO.data());
        } else {
            transacao.setData(LocalDateTime.now());
        }


        if (transacaoDTO.statusTransacao() != StatusTransacao.REPROVADA &&
                transacaoDTO.statusTransacao() != StatusTransacao.APROVADA) {

            throw new RegraDeNegociosException("Status incorreto");
        }

        if (transacaoDTO.tipoTransacao() != TipoTransacao.CREDITO &&
                transacaoDTO.tipoTransacao() != TipoTransacao.DEBITO) {

            throw new RegraDeNegociosException("Tipo transacao incorreto");
        }


        transacao.setValor(transacaoDTO.valor());
        transacao.setUsuario(usuario);
        transacao.setTipoTransacao(transacaoDTO.tipoTransacao());
        transacao.setStatusTransacao(transacaoDTO.statusTransacao());

        Transacao salva = transacaoRepository.save(transacao);


        return new TransacaoDTO(
                salva.getIdTransacao(),
                salva.getTipoTransacao(),
                salva.getValor(),
                salva.getStatusTransacao(),
                salva.getData(),
                salva.getUsuario().getIdUsuario()
        );

    }

    public TransacaoDTO atualizarTransacao(Long id, TransacaoDTO transacaoDTO) {

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Transação não encontrada"));


        if (transacaoDTO.idUsuario() != null) {
            Usuario usuario = usuarioService.buscarPorId(transacaoDTO.idUsuario());;

            transacao.setUsuario(usuario);


            //atualizo o saldo depois do fim
            //todo -> verificar se tem saldo suficiente antes de iniciar
            // todo -> alterar status para concluido (ver codigo adicional)
            if(transacaoDTO.tipoTransacao() == TipoTransacao.CREDITO){
                BigDecimal saldoUsuario = usuario.getSaldo().add(transacaoDTO.valor());
                usuario.setSaldo(saldoUsuario);
            } else if(transacaoDTO.tipoTransacao() == TipoTransacao.DEBITO){
                BigDecimal saldoUsuario = usuario.getSaldo().subtract(transacaoDTO.valor());
                usuario.setSaldo(saldoUsuario);
            }

        }


        if(transacaoDTO.valor() != null){
            transacao.setValor(transacaoDTO.valor());
        }

        if(transacaoDTO.tipoTransacao() != null){
            transacao.setTipoTransacao(transacaoDTO.tipoTransacao());
        }

        if(transacaoDTO.statusTransacao() != null){
            transacao.setStatusTransacao(transacaoDTO.statusTransacao());
        }



        Transacao atualizada = transacaoRepository.save(transacao);

        return new TransacaoDTO(
                atualizada.getIdTransacao(),
                atualizada.getTipoTransacao(),
                atualizada.getValor(),
                atualizada.getStatusTransacao(),
                atualizada.getData(),
                atualizada.getUsuario().getIdUsuario()
        );


    }

    public List<TransacaoDTO> listarPorUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Transacao> transacoes = transacaoRepository.findByUsuarioIdUsuario(idUsuario);

        if (transacoes.isEmpty()) {
            throw new RegraDeNegociosException("Nenhuma transação encontrada para o usuário");
        }

        List<TransacaoDTO> listaDTO = new ArrayList<>();

        for (Transacao transacao : transacoes) {

            TransacaoDTO dto = new TransacaoDTO(
                    transacao.getIdTransacao(),
                    transacao.getTipoTransacao(),
                    transacao.getValor(),
                    transacao.getStatusTransacao(),
                    transacao.getData(),
                    transacao.getUsuario().getIdUsuario()
            );

            listaDTO.add(dto);
        }

        return listaDTO;
    }

}
