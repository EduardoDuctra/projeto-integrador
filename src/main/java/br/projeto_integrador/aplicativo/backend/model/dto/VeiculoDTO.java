package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO contendo as informações do veículo")
public record VeiculoDTO(Long idVeiculo,
                         String modeloCarro,
                         String nomeMarca) {
}
