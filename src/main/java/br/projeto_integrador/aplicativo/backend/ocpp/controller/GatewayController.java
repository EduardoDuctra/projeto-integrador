package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.ocpp.dto.*;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.ConectorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


//é essa classe que entra os dados servidor em python
@RestController
@RequestMapping("/backend")
public class GatewayController {

    private final CarregadorService carregadorService;
    private final ConectorService conectorService;
    private final TransacaoService transacaoService;


    public GatewayController(CarregadorService carregadorService, ConectorService conectorService, TransacaoService transacaoService, SecurityUtils securityUtils, OcppClientService ocppClientService) {
        this.carregadorService = carregadorService;
        this.conectorService = conectorService;
        this.transacaoService = transacaoService;
    }

    //info do carregador
    @PostMapping("/bootNotification")
    public ResponseEntity<Void> bootNotification(@RequestBody BootNotificationDTO payload) {

        System.out.println("Recebido BootNotification:");
        System.out.println(payload);

        carregadorService.processarCarregador(payload);

        return ResponseEntity.ok().build();
    }


    //Estado atual de cada conector (Available, Charging, Faulted, etc.)
    @PostMapping("/statusNotification")
    public ResponseEntity<Void> statusNotification(@RequestBody StatusNotificationDTO payload) {

        System.out.println("Recebido StatusNotification:");
        System.out.println(payload);

        conectorService.processarConector(payload);

        return ResponseEntity.ok().build();
    }


    //dizer se o carregador está ativo
    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestBody HeartbeatDTO payload) {

        System.out.println("Recebido heartbeat:");
        System.out.println(payload);


        return ResponseEntity.ok().build();
    }


    @PostMapping("/meterValues")
    public ResponseEntity<Void> meterValues(@RequestBody MeterValuesCompletoDTO payload) {

        System.out.println("Recebido meterValues:");
        System.out.println(payload);

        conectorService.processarMeterValues(payload);
        transacaoService.processarMeterValuesCiclo(payload);

        return ResponseEntity.ok().build();
    }


    //todo DTO
    @PostMapping("/startTransaction")
    public ResponseEntity<Void> startTransaction(@RequestBody Map<String, Object> payload) {

        System.out.println("Recebido StartTransaction:");
        System.out.println(payload);

        return ResponseEntity.ok().build();
    }

}
