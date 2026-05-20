package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que permite aletar as informações do Conector")
public record AtualizarConectorDTO(
        String charger_id,
        Integer connector_id,
        TipoConector tipoConector,
        NomeConector nomeConector

) {
}
