package br.projeto_integrador.aplicativo.backend.websocket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransacaoSubject {

    private final List<Observer> observers;

    public TransacaoSubject(List<Observer> observers) {
        this.observers = observers;
    }

    public void notificar(Long idUsuario){
        for (Observer o : observers){
            o.atualizar(idUsuario);
        }
    }

    public void notificarCarregador(String idCarregador){
        for (Observer o : observers){
            o.atualizarCarregador(idCarregador);
        }
    }
}