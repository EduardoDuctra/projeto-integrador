package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.ocpp.dto.BootNotificationDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


//é essa classe que entra o servidor em python
@RestController
@RequestMapping("/backend")
public class ServerController {

    private final CarregadorService carregadorService;

    public ServerController(CarregadorService carregadorService) {
        this.carregadorService = carregadorService;
    }

    @PostMapping("/bootNotification")
    public ResponseEntity<Void> bootNotification(@RequestBody BootNotificationDTO payload) {

        System.out.println("Recebido BootNotification:");
        System.out.println(payload);

        carregadorService.processarCarregador(payload);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/statusNotification")
    public ResponseEntity<Void> statusNotification(@RequestBody Map<String, Object> payload) {

        System.out.println("Recebido StatusNotification:");
        System.out.println(payload);

        return ResponseEntity.ok().build();
    }
}
