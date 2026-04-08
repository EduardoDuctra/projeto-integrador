package br.projeto_integrador.aplicativo.backend.ocpp.dto;

public record BootNotificationDTO(
        String charger_id,
        String charge_point_vendor,
        String charge_point_model,
        String charge_box_serial_number,
        String charge_point_serial_number,
        String firmware_version,
        String iccid
) {}