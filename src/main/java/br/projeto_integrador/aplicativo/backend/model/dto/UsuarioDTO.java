package br.projeto_integrador.aplicativo.backend.model.dto;


import java.math.BigDecimal;


public record UsuarioDTO(Long idUsuario,
                         String nome,
                         String cpf,
                         String telefone,
                         String email,
                         String fotoUrl,
                         BigDecimal saldo,
                         Boolean isCadastroCompleto,

                         //post e put
                         Long idVeiculoPrincipal,

                         //get
                         String modeloVeiculoPrincipal

) {
}
