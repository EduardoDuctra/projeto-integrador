package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record OnMeterValueDTO(
        @JsonProperty("charger_id")
        String chargerId,

        @JsonProperty("connector_id")
        Integer connectorId,

        @JsonProperty("transaction_id")
        Long transactionId,

        @JsonProperty("meter_value")
        Map<String, Object> meterValue
) {}