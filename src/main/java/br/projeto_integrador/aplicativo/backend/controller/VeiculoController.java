package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import br.projeto_integrador.aplicativo.backend.services.VeiculoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    private final VeiculoService service;
    private final SecurityUtils securityUtils;

    public VeiculoController(VeiculoService service,
                             UsuarioService usuarioService, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<VeiculoDTO> cadastraOuAtualizarVeiculoAoUsuario(HttpServletRequest request, @RequestBody VeiculoDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        VeiculoDTO veiculo = service.cadastrarOuAtualizarVeiculoAoUsuario(id, dto);

        return ResponseEntity.status(201).body(veiculo);

    }

    @GetMapping("/listar")
    public ResponseEntity<VeiculoDTO>listarVeiculoPorUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        VeiculoDTO veiculo = service.listarVeiculoUsuario(id);

        return ResponseEntity.status(201).body(veiculo);

    }





}
