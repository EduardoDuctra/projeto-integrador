package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

//saida -> response
@Schema(description = "DTO de resposta após criar o usuário")
public record UsuarioResponseDTO(Long idUsuario,
                                 String nome,
                                 String email
                         ) {
}