package br.projeto_integrador.aplicativo.backend.model.dto;

public record UsuarioCadastroDTO(Long idUsuario,
                         String nome,
                         String cpf,
                         String telefone,
                         String email,
                         String senha) {
}
