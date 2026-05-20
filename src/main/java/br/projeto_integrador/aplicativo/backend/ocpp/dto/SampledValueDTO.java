package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO contendo dados elétricos medidos durante a recarga")
public record SampledValueDTO(
        String unit,
        String context,
        String measurand,
        String phase,
        String location,
        String format,
        Double value
) {}