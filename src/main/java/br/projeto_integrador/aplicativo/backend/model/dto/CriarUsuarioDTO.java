package br.projeto_integrador.aplicativo.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO utilizado para criar usuários")
public record CriarUsuarioDTO(Long idUsuario,
                              String nome,
                              String cpf,
                              String telefone,
                              String email,
                              String fotoUrl,
                              BigDecimal saldo,
                              String senha) {
}
