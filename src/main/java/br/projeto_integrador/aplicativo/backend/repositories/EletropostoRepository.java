package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Eletroposto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EletropostoRepository extends JpaRepository <Eletroposto, Long> {
}
