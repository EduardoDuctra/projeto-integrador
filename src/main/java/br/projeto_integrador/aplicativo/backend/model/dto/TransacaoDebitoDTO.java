package br.projeto_integrador.aplicativo.backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDebitoDTO(BigDecimal valorRecarga,
                                 Double recargaKwh,
                                 LocalDateTime dataInicio,
                                 String modelo) {
}
