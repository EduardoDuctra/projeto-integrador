package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository <Veiculo, Long> {

    Optional<Veiculo> findByUsuario_IdUsuario(Long idUsuario);

    List<Veiculo> findAllByUsuario_IdUsuario(Long idUsuario);

}






