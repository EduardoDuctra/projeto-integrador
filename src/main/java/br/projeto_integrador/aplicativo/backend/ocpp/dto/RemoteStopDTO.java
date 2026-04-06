package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoteStopDTO(
        @JsonProperty("charger_id")
        String chargerId,

        @JsonProperty("transaction_id")
        Long transactionId) {
}
