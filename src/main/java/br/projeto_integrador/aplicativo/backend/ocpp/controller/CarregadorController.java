package br.projeto_integrador.aplicativo.backend.ocpp.controller;


import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carregador")
public class CarregadorController {

    private final OcppClientService ocppClientService;


    public CarregadorController(OcppClientService ocppClientService) {
        this.ocppClientService = ocppClientService;
    }

    //python retorna uma string
    @PostMapping("/remotestart")
    public ResponseEntity<String> remoteStart(@RequestBody RemoteStartDTO remoteStartDTO) {

        String response = ocppClientService.iniciarRecarga(remoteStartDTO);

        return ResponseEntity.status(200).body(response);

    }


    @PostMapping("/remotestop")
    public ResponseEntity<String> remoteStop(@RequestBody RemoteStopDTO remoteStopDTO) {

        String response = ocppClientService.pararRecarga(remoteStopDTO);

        return ResponseEntity.status(200).body(response);

    }


    @PostMapping("/unlockconnector")
    public ResponseEntity<String> unlockConnector(@RequestBody UnlockConnectorDTO unlockConnectorDTO) {

        String response = ocppClientService.desbloquearConector(unlockConnectorDTO);

        return ResponseEntity.status(200).body(response);

    }
}


