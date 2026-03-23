package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository <VeiculoEntity, Long> {

    Optional<VeiculoEntity> findByUsuario_IdUsuario(Long idUsuario);}
