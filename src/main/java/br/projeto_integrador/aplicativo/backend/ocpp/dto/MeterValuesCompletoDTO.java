package br.projeto_integrador.aplicativo.backend.ocpp.dto;

public record MeterValuesDTO(
        String charger_id,
        Integer connector_id,
        Integer transaction_id,
        List<MeterValueDTO> meter_value
) {}