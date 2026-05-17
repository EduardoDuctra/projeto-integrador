package br.projeto_integrador.aplicativo.backend.model.dto;

//saida -> response
public record UsuarioResponseDTO(Long idUsuario,
                                 String nome,
                                 String email
                         ) {
}