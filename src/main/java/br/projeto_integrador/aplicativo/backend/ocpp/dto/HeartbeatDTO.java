package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para monitorar o status de atividade do carregador")
public record HeartbeatDTO(
        String charger_id,
        String heartbeat) {
}
