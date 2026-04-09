package br.projeto_integrador.aplicativo.backend.ocpp.dto;

public record SampledValueDTO(
        String unit,
        String context,
        String measurand,
        String phase,
        String location,
        String format,
        Double value
) {}