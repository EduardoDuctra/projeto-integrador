package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para listar carregadores e suas informações no frontend")
public record CarregadorDTO(String idCarregador,
                            StatusCarregador statusCarregador,
                            String cidade) {
}
