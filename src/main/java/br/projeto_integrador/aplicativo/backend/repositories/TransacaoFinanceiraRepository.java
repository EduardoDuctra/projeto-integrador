package br.projeto_integrador.aplicativo.backend.repositories;

import br.projeto_integrador.aplicativo.backend.model.entity.TransacaoFinanceira;
import br.projeto_integrador.aplicativo.backend.model.entity.TransacaoRecarga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoFinanceiraRepository extends JpaRepository <TransacaoFinanceira, Long> {

    List<TransacaoFinanceira> findByUsuarioIdUsuario(Long idUsuario);
}
