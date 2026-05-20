package br.projeto_integrador.aplicativo.backend.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para validação de uma nova senha")
public record NovaSenhaDTO(String email,
                           String codigo,
                           String novaSenha) {
}


