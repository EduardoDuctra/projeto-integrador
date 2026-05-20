package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO utilizado para receber o valor máximo da recarga enviado pelo frontend")
public record AtualizarValorMaximoDTO(BigDecimal valorMaximo) {
}
