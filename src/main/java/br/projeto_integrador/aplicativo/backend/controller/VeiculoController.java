package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.VeiculoService;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/veiculo")
@Tag(name = "Veículo", description = "Path relacionado aos veículos")
public class VeiculoController {

    private final VeiculoService service;
    private final SecurityUtils securityUtils;
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService service,
                             SecurityUtils securityUtils, VeiculoService veiculoService) {
        this.service = service;
        this.securityUtils = securityUtils;
        this.veiculoService = veiculoService;
    }


    /**
     * cadastrar/atualizar um veículo ao usuário
     * @param request
     * @param dto
     * @return
     */
    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar um veículo novo", description = "Cadastrar um veículo novo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class))),
    })
    public ResponseEntity<VeiculoDTO> cadastraOuAtualizarVeiculoAoUsuario(HttpServletRequest request, @RequestBody VeiculoDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        VeiculoDTO veiculo = service.cadastrarVeiculoAoUsuario(id, dto);

        return ResponseEntity.status(201).body(veiculo);

    }

    /**
     * lista os veículos do usuário
     * @param request
     * @return
     */
    @GetMapping("/listar")
    @Operation(summary = "Listar os veículo do usuário", description = "Listar os veículo do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VeiculoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum veículo encontrado")

    })
    public ResponseEntity<List<VeiculoDTO>>listarVeiculoPorUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<VeiculoDTO> veiculos = service.listarVeiculoUsuario(id);

        if(veiculos.isEmpty()){

            return ResponseEntity.status(404).body(veiculos);
        }

        return ResponseEntity.status(200).body(veiculos);

    }


    @DeleteMapping("/deletar/{idVeiculo}")
    @Operation(summary = "Deletar um veículo", description = "Deletar um veículo. Recebe o ID do veículo pela URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")

    })
    public ResponseEntity<Void> deletarVeiculo(HttpServletRequest request, @PathVariable Long idVeiculo) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        veiculoService.deletarVeiculo(idVeiculo);

        return ResponseEntity.noContent().build();
    }





}
