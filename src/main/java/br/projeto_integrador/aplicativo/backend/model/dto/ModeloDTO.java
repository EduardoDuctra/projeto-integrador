package br.projeto_integrador.aplicativo.backend.model.dto;


public record ModeloDTO(
        Long idModelo,
        String modelo,
        MarcaDTO marca
) {}