package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para receber o token JWT fornecido pelo Google Sign-In")
public record TokenGoogleDTO(String token) {}