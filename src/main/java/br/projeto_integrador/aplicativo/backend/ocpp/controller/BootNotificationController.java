package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


//é essa classe que entra o servidor em python
@RestController
@RequestMapping("/backend")
public class BootNotificationController {

    @PostMapping("/bootNotification")
    public ResponseEntity<Void> bootNotification(@RequestBody Map<String, Object> payload) {

        System.out.println("Recebido BootNotification:");
        System.out.println(payload);

        return ResponseEntity.ok().build();
    }
}
