package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "DTO utilizado para registrar medições de carregamento")
public record MeterValueDTO(
        List<SampledValueDTO> sampled_value,
        OffsetDateTime timestamp
) {}