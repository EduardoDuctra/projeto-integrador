package br.projeto_integrador.aplicativo.backend.ocpp.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carregador")
public class CarregadorController {

    private final OcppClientService ocppClientService;
    private final CarregadorService carregadorService;

    public CarregadorController(OcppClientService ocppClientService, CarregadorService carregadorService) {
        this.ocppClientService = ocppClientService;
        this.carregadorService = carregadorService;
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


    @PutMapping("/atualizar-carregador")
    public ResponseEntity<String> atualizarInformacoes(@RequestBody AtualizarCarregadorDTO dto){

        String response = carregadorService.atualizarInformacoes(dto);

        return ResponseEntity.status(200).body(response);
    }
}


