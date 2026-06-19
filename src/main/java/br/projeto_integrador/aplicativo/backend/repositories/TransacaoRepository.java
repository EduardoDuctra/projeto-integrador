package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository <Transacao, Long> {

    List<Transacao> findByUsuarioIdUsuario(Long idUsuario);

    Optional<Transacao> findByIdTransacao(Long idTransacao);

    Optional<Transacao> findTopByConectorAndStatusTransacao(Conector conector, StatusTransacao statusTransacao);

    Optional<Transacao> findTopByUsuarioAndStatusTransacao(Usuario usuario, StatusTransacao statusTransacao);

    Optional<Transacao> findTopByUsuarioOrderByDataFimDesc(Usuario usuario);

    Optional<Transacao> findTopByUsuarioAndDataFimIsNotNullOrderByDataFimDesc(Usuario usuario);

    List<Transacao> findByConectorAndStatusTransacao(Conector conector, StatusTransacao statusTransacao);


    //evitar concorrência
    // PESSIMISTIC_WRITE -> trava a transação no BD
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transacao t WHERE t.id = :id")
    Optional<Transacao> buscarPorIdComLock(@Param("id") Long id);

}
