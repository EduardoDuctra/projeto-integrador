package br.projeto_integrador.aplicativo.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CodigoRecuperacao {
    private String codigo;
    private LocalDateTime validade;
}
