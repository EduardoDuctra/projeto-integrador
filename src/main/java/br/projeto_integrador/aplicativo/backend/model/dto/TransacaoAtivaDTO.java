package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoAtivaDTO(Long id,
                                Long idTransacao,
                                StatusTransacao statusTransacao,
                                BigDecimal valorRecarga,
                                BigDecimal valorMaximo,
                                LocalDateTime dataInicio,
                                Double socAtual,
                                Integer connectorId,
                                String idCarregador) {
}
