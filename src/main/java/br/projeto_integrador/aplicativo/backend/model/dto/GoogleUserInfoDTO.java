package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado na autenticação via Google, com campos diferentes da autenticação tradicional")
public record GoogleUserInfoDTO(String email,
                                String nome) {
}
