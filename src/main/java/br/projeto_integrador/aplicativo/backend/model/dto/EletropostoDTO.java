package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusEletroposto;

public record EletropostoDTO(Long idEletroposto,
                             String nomeEletroposto,
                             String cidade,
                             String uf,
                             String endereco,
                             StatusEletroposto statusEletroposto) {
}

