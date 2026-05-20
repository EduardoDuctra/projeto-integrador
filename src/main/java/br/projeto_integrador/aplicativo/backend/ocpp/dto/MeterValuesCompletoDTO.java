package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO principal de medições do carregador")
public record MeterValuesCompletoDTO(
        String charger_id,
        Integer connector_id,
        Integer transaction_id,
        List<MeterValueDTO> meter_value
) {}