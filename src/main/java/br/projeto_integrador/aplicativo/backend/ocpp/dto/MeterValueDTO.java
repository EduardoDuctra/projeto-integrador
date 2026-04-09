package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import java.time.OffsetDateTime;

public record MeterValueDTO(
        List<SampledValueDTO> sampled_value,
        OffsetDateTime timestamp
) {}