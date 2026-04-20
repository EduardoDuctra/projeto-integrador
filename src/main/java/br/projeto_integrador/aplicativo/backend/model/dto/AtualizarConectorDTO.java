package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;

public record AtualizarConectorDTO(
        String charger_id,
        Integer connector_id,
        TipoConector tipoConector,
        NomeConector nomeConector

) {
}
