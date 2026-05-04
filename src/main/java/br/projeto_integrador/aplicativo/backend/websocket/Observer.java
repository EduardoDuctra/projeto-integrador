package br.projeto_integrador.aplicativo.backend.websocket;

import java.math.BigDecimal;

public interface Observer {

    void atualizar(Long idUsuario);

    void atualizarCarregador(String idCarregador);

    void atualizarTodosCarregadores();

    void atualizarSaldo(Long idUsuario, BigDecimal saldo);

}
