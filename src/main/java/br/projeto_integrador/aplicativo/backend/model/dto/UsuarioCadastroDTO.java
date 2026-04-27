package br.projeto_integrador.aplicativo.backend.model.dto;


//post e put
public record UsuarioCadastroDTO(Long idUsuario,
                                 String nome,
                                 String cpf,
                                 String telefone,
                                 String email,
                                 String fotoUrl,
                                 String senha) {
}
