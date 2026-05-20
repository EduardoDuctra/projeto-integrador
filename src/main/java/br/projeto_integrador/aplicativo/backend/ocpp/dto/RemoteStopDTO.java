package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado PARAR uma recarga")
public record RemoteStopDTO(
        @JsonProperty("charger_id")
        String chargerId,

        @JsonProperty("transaction_id")
        Long transactionId) {
}
