package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import java.util.List;

public record MeterValuesCompletoDTO(
        String charger_id,
        Integer connector_id,
        Integer transaction_id,
        List<MeterValueDTO> meter_value
) {}