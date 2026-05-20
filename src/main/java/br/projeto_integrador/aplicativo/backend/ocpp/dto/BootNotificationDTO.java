package br.projeto_integrador.aplicativo.backend.ocpp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO contendo os dados de identificação enviados pelo CARREGADOR no Boot Notification")
public record BootNotificationDTO(
        String charger_id,
        String charge_point_vendor,
        String charge_point_model,
        String charge_box_serial_number,
        String charge_point_serial_number,
        String firmware_version,
        String iccid
) {}