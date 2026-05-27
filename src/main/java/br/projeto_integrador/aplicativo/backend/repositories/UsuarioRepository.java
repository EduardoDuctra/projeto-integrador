package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    public Usuario findByEmail(String email);


    //evitar concorrência
    // PESSIMISTIC_WRITE -> trava a transação no BD
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Optional<Usuario> buscarPorIdComLock(@Param("id") Long id);


}
