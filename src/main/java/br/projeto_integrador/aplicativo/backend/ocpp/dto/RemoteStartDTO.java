package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoteStartDTO(
        @JsonProperty("charger_id")
        String chargerId,

        @JsonProperty("connector_id")
        Integer connectorId) {
}
