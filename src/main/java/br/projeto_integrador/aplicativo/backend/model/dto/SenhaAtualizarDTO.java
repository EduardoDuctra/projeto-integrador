package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para atualizar a senha")
public record SenhaAtualizarDTO (String senha){ }
