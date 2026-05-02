package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;

public record ConectorDTO(Long id,
                          Integer connectorIdNoCarregador,
                          TipoConector tipo,
                          boolean disponivelUso,
                          NomeConector nomeConector,
                          String idCarregador) {
}
