package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConectorRepository extends JpaRepository<Conector, Long> {
}
