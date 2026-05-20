package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.*;
import br.projeto_integrador.aplicativo.backend.ocpp.service.ConectorService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conector")
@Tag(name = "Conector", description = "Path relacionado ao conector")
public class ConcectorController {

    private final ConectorService conectorService;
    private final SecurityUtils securityUtils;



    public ConcectorController(ConectorService conectorService, SecurityUtils securityUtils) {
        this.conectorService = conectorService;
        this.securityUtils = securityUtils;
    }


    /**
     * atualiza os conectores
     * usado para dizer o tipo CC/CA
     * @param dto
     * @return
     */
    @PutMapping("/atualizar-conector")
    @Operation(summary = "Atualizar informações do conector", description = "Atualizar informações do conector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conector atualizado com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> atualizarInformacoes(@RequestBody AtualizarConectorDTO dto){

        String response = conectorService.atualizarInformacoes(dto);

        return ResponseEntity.status(200).body(response);
    }


    /**
     * recebe o id do usuario e lista o que ele usou recentemetne
     * usado para poder liberar forçado, se precisar
     * @param request
     * @return ConectorDTO
     */
    @GetMapping("/usado-recentemente-pelo-usuario")
    @Operation(summary = "Listar o conector usado recentemente pelo usuário",
            description = "Listar o conector usado recentemente pelo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conector atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConectorDTO.class))),
    })
    public ResponseEntity<ConectorDTO> conectorUsadoRecentemente(HttpServletRequest request){

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        ConectorDTO dto = conectorService.listarTransacaoAtivaRecentementePorUsuario(id);


        if (dto == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(dto);
    }

    /**
     * lista os conectores de cada carregador disponível
     * @param idCarregador
     * @return List<ConectorDTO>
     */
    @GetMapping("/disponiveis/{idCarregador}")
    @Operation(summary = "Listar os conectores disponíveis",
            description = "Listar os conectores disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conectores encontrados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConectorDTO.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum conector disponível encontrado")
    })
    public ResponseEntity<List<ConectorDTO>>conectoresDisponiveis(@PathVariable String idCarregador){

        List<ConectorDTO> lista = conectorService.conectoresPorCarregador(idCarregador);


        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(lista);
    }


}



