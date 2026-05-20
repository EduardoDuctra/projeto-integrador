package br.projeto_integrador.aplicativo.backend.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO contendo as informações completas do usuário")
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
