package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para listar conectores e suas informações no frontend")
public record ConectorDTO(Long id,
                          Integer connectorIdNoCarregador,
                          TipoConector tipo,
                          boolean disponivelUso,
                          Double valorEnergia,
                          NomeConector nomeConector,
                          String idCarregador) {
}
