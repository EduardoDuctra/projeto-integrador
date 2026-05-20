package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para enviar um email com o código de recuperação")
public record EsqueciSenhaDTO(String email) {
}
