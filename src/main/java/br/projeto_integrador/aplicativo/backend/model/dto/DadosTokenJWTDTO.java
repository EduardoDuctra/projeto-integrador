package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO responsável pelo token JWT de autenticação")
public record DadosTokenJWTDTO(String token) {

}