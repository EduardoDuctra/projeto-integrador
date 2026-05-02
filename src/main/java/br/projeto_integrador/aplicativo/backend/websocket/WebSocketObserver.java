package br.projeto_integrador.aplicativo.backend.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketObserver implements Observer {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketObserver(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }



    @Override
    public void atualizar(Long idUsuario) {
        messagingTemplate.convertAndSend(
                "/topic/usuario/" + idUsuario,
                "atualização de estado"
        );
    }

    @Override
    public void atualizarCarregador(String idCarregador) {
        messagingTemplate.convertAndSend(
                "/topic/carregador/" + idCarregador,
                "atualização de estado"
        );
    }

    @Override
    public void atualizarTodosCarregadores() {
        messagingTemplate.convertAndSend(
                "/topic/carregadores",
                "atualizacao"
        );
    }


}
