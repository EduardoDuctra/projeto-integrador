package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.VeiculoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    private final VeiculoService service;
    private final SecurityUtils securityUtils;

    public VeiculoController(VeiculoService service,
                              SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }


    /**
     * cadastrar/atualizar um veículo ao usuário
     * @param request
     * @param dto
     * @return
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<VeiculoDTO> cadastraOuAtualizarVeiculoAoUsuario(HttpServletRequest request, @RequestBody VeiculoDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        VeiculoDTO veiculo = service.cadastrarOuAtualizarVeiculoAoUsuario(id, dto);

        return ResponseEntity.status(201).body(veiculo);

    }

    /**
     * lista os veículos do usuário
     * @param request
     * @return
     */
    @GetMapping("/listar")
    public ResponseEntity<List<VeiculoDTO>>listarVeiculoPorUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<VeiculoDTO> veiculos = service.listarVeiculoUsuario(id);

        if(veiculos.isEmpty()){

            return ResponseEntity.status(404).body(veiculos);
        }

        return ResponseEntity.status(201).body(veiculos);

    }





}
