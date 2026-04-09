package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConectorRepository extends JpaRepository<Conector, Integer> {

    Optional<Conector> findByCarregador_IdCarregadorAndConnectorIdNoCarregador(
            String idCarregador, Integer connectorId);

}
