package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO utilizado informar ao usuário os dados da transação ativa no momento")
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
