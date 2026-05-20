package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que permite aletar as informações do Carregador")
public record AtualizarCarregadorDTO(
        String idCarregador,
        Double potenciaCorrenteContinua,
        Double potenciaCorrenteAlternada,
        String cidade,
        String endereco
) {
}
