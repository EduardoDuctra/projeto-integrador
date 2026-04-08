package br.projeto_integrador.aplicativo.backend.model.dto;

public record AtualizarCarregadorDTO(
        String idCarregador,
        Double potenciaCorrenteContinua,
        Double potenciaCorrenteAlternada,
        Long idEletroposto

) {
}
