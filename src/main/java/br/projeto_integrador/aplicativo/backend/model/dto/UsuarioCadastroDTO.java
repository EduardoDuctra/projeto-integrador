package br.projeto_integrador.aplicativo.backend.model.dto;

import java.math.BigDecimal;

public record UsuarioCadastroDTO(Long idUsuario,
                                 String nome,
                                 String cpf,
                                 String telefone,
                                 String email,
                                 String senha,
                                 BigDecimal saldo) {
}
