package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO responsável pelas transações de Débito")
public record TransacaoDebitoDTO(BigDecimal valorRecarga,
                                 Double recargaKwh,
                                 LocalDateTime dataInicio,
                                 String modelo) {
}
