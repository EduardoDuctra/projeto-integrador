package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado FORÇAR A RETIRADA do conector")
public record UnlockConnectorDTO(
        @JsonProperty("charger_id")
        String chargerId,

        @JsonProperty("connector_id")
        Integer connectorId) {
}
