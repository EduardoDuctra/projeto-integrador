package br.projeto_integrador.aplicativo.backend.mercadoPago;


public record WebhookDTO(String type, Data data) {

    public record Data(Long id) {
    }
}