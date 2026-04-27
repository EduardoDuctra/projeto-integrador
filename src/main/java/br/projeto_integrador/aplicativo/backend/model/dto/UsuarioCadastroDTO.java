package br.projeto_integrador.aplicativo.backend.model.dto;


import java.math.BigDecimal;

//post e put
public record UsuarioCadastroDTO(Long idUsuario,
                                 String nome,
                                 String cpf,
                                 String telefone,
                                 String email,
                                 String fotoUrl,
                                 BigDecimal saldo,
                                 String senha) {
}
