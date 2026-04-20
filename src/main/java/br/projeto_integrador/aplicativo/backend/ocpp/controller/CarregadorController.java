package br.projeto_integrador.aplicativo.backend.ocpp.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//envia os dados para o servidor
@RestController
@RequestMapping("/carregador")
public class CarregadorController {

    private final OcppClientService ocppClientService;
    private final CarregadorService carregadorService;
    private final SecurityUtils securityUtils;

    public CarregadorController(OcppClientService ocppClientService, CarregadorService carregadorService, SecurityUtils securityUtils) {
        this.ocppClientService = ocppClientService;
        this.carregadorService = carregadorService;
        this.securityUtils = securityUtils;
    }

    //python retorna uma string
    @PostMapping("/remotestart")
    public ResponseEntity<String> remoteStart(HttpServletRequest request,
                                              @RequestBody RemoteStartDTO remoteStartDTO) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String response = ocppClientService.iniciarRecarga(id, remoteStartDTO);

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


