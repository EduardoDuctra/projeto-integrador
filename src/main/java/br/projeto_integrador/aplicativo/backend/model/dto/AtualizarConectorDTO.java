package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.TipoCarregador;

public record AtualizarConectorDTO(
        String charger_id,
        Integer connector_id,
        TipoCarregador tipoCarregador
) {
}
