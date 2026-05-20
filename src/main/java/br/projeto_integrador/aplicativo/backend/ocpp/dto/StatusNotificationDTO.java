package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO contendo o estado atual dos conectores do carregador")
public record StatusNotificationDTO(
        String charger_id,
        Integer connector_id,
        String error_code,
        StatusNotification status,
        String  timestamp,
        String info,
        String vendor_id,
        String vendor_error_code) {
}
