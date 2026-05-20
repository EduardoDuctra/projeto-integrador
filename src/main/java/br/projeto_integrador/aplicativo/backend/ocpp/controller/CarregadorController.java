package br.projeto_integrador.aplicativo.backend.ocpp.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.CarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ConectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.CarregadorService;
import br.projeto_integrador.aplicativo.backend.ocpp.service.OcppClientService;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//envia os dados para o servidor
@RestController
@RequestMapping("/carregador")
@Tag(name = "Carregador", description = "Path relacionado ao carregador")
public class CarregadorController {

    private final OcppClientService ocppClientService;
    private final CarregadorService carregadorService;
    private final SecurityUtils securityUtils;

    public CarregadorController(OcppClientService ocppClientService, CarregadorService carregadorService,
                                SecurityUtils securityUtils) {
        this.ocppClientService = ocppClientService;
        this.carregadorService = carregadorService;
        this.securityUtils = securityUtils;
    }

    //python retorna uma string
    @PostMapping("/remotestart")
    @Operation(summary = "Enviar o comando de iniciar recarga",
            description = "Enviar o comando de iniciar recarga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Erro ao publicar em remotestart"),
    })
    public ResponseEntity<String> remoteStart(HttpServletRequest request,
                                              @RequestBody RemoteStartDTO remoteStartDTO) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String response = ocppClientService.iniciarRecarga(id, remoteStartDTO);

        return ResponseEntity.status(200).body(response);

    }


    @PostMapping("/remotestop")
    @Operation(summary = "Enviar o comando de parar recarga",
            description = "Enviar o comando de parar recarga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Erro ao publicar em remoteStop"),
    })
    public ResponseEntity<String> remoteStop(@RequestBody RemoteStopDTO remoteStopDTO) {

        String response = ocppClientService.pararRecarga(remoteStopDTO);

        return ResponseEntity.status(200).body(response);

    }


    @PostMapping("/unlockconnector")
    @Operation(summary = "Enviar o comando de forçar desconectar",
            description = "Enviar o comando de forçar desconectar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Erro ao publicar em unlockConnector"),
    })
    public ResponseEntity<String> unlockConnector(@RequestBody UnlockConnectorDTO unlockConnectorDTO) {

        String response = ocppClientService.desbloquearConector(unlockConnectorDTO);

        return ResponseEntity.status(200).body(response);

    }


    @PutMapping("/atualizar-carregador")
    @Operation(summary = "Atualizar informações do carregador", description = "Atualizar informações do carregador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carregador atualizado com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> atualizarInformacoes(@RequestBody AtualizarCarregadorDTO dto){

        String response = carregadorService.atualizarInformacoes(dto);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar os carregadores disponíveis",
            description = "Listar os carregadores disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carregadores encontrados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarregadorDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum carregador disponível"),

    })
    public ResponseEntity<List<CarregadorDTO>>conectoresDisponiveis(){

        List<CarregadorDTO> lista = carregadorService.listarCarregadoresDisponiveis();


        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(lista);
    }
}


