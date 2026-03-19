package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.ModeloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloRepository extends JpaRepository <ModeloEntity, Long> {
}
