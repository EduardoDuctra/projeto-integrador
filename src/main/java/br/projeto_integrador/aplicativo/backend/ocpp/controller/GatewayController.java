package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.DadosAutenticacaoDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.*;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.ConectorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gateway", description = "Path relacionado a entrada de dados do servidor OCPP")
public class GatewayController {

    private final CarregadorService carregadorService;
    private final ConectorService conectorService;
    private final TransacaoService transacaoService;


    public GatewayController(CarregadorService carregadorService, ConectorService conectorService,
                             TransacaoService transacaoService) {
        this.carregadorService = carregadorService;
        this.conectorService = conectorService;
        this.transacaoService = transacaoService;
    }

    //info do carregador
    @PostMapping("/bootNotification")
    @Operation(summary = "Informações do carregador", description = "Informações do carregador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<Void> bootNotification(@RequestBody BootNotificationDTO payload) {

        System.out.println("Recebido BootNotification:");
        System.out.println(payload);

        carregadorService.processarCarregador(payload);

        return ResponseEntity.ok().build();
    }


    //Estado atual de cada conector (Available, Charging, Faulted, etc.)
    @PostMapping("/statusNotification")
    @Operation(summary = "Estado atual de cada conector", description = "Estado atual de cada conector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<Void> statusNotification(@RequestBody StatusNotificationDTO payload) {

        System.out.println("Recebido StatusNotification:");
        System.out.println(payload);

        conectorService.processarConector(payload);

        //para ver se tem um CC sendo utilizado -> outro fica indisponível
        conectorService.atualizarDisponivelUsoCC(payload.charger_id());

        //atualizar status carregador
        carregadorService.atualizarStatusCarregadores(payload.charger_id());

        return ResponseEntity.ok().build();
    }


    //dizer se o carregador está ativo
    @PostMapping("/heartbeat")
    @Operation(summary = "Monitorar o status de atividade do carregador", description = "Monitorar o status de atividade do carregador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<Void> heartbeat(@RequestBody HeartbeatDTO payload) {

        System.out.println("Recebido heartbeat:");
        System.out.println(payload);



        return ResponseEntity.ok().build();
    }


    @PostMapping("/meterValues")
    @Operation(summary = "Registro de informações de medições de carregamento", description = "Registro de informações de medições de carregamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
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

    //todo DTO
    @PostMapping("/stopTransaction")
    public ResponseEntity<Void> stopTransaction(@RequestBody Map<String, Object> payload) {

        System.out.println("Recebido stopTransaction:");
        System.out.println(payload);

        return ResponseEntity.ok().build();
    }

}
