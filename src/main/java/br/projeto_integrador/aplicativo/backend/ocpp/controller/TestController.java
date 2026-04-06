package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TestController {

    private final OcppClientService service;

    public TestController(OcppClientService service) {
        this.service = service;
    }

    @GetMapping
    public String testar() {
        return service.testarConexao();
    }
}