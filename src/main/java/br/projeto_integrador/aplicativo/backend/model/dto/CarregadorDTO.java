package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;

public record CarregadorDTO(String idCarregador,
                            StatusCarregador statusCarregador,
                            String cidade) {
}
