package br.projeto_integrador.aplicativo.backend.model.dto;

//saida -> response
public record UsuarioDTO (Long idUsuario,
                         String nome,
                         String email
                         ) {
}