package br.projeto_integrador.aplicativo.backend.ocpp.dto;

public record HeartbeatDTO(
        String charger_id,
        String heartbeat) {
}
