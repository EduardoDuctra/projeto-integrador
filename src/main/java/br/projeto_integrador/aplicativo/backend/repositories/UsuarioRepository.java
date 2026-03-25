package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    public Usuario findByEmail(String email);


}
