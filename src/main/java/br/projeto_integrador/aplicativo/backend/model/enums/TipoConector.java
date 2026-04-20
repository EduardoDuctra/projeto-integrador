package br.projeto_integrador.aplicativo.backend.model.enums;

import lombok.Getter;

@Getter
public enum TipoConector {
    CC (1.9),
    CA (0.9);

    private final double valorKwh;

    TipoConector(double valorKwh) {
        this.valorKwh = valorKwh;
    }
}
