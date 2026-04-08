package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.Carregador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarregadorRepository extends JpaRepository <Carregador, String> {
}
