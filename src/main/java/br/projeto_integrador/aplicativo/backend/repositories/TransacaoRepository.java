package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository <Transacao, Long> {

    List<Transacao> findByUsuarioIdUsuario(Long idUsuario);

    Optional<Transacao> findByIdTransacao(Long idTransacao);

    Optional<Transacao> findTopByConectorAndStatusTransacao(Conector conector, StatusTransacao statusTransacao);

    Optional<Transacao> findTopByUsuarioAndStatusTransacao(Usuario usuario, StatusTransacao statusTransacao);

}
