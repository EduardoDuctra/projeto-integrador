package br.projeto_integrador.aplicativo.backend.model.dto;


import java.math.BigDecimal;


public record UsuarioCompletoDTO(Long idUsuario,
                                 String nome,
                                 String cpf,
                                 String telefone,
                                 String email,
                                 String fotoUrl,
                                 BigDecimal saldo,
                                 String senha,


                                 //post e put
                                 Long idVeiculoPrincipal,

                                 //get
                                 String modeloVeiculoPrincipal

) {
}
