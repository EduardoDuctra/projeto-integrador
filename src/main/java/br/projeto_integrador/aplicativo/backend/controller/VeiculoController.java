package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import br.projeto_integrador.aplicativo.backend.services.VeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    private final VeiculoService service;
    private final UsuarioService usuarioService;

    public VeiculoController(VeiculoService service,
                             UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }


    @PostMapping("/cadastrar/{idUsuario}")
    public ResponseEntity<VeiculoDTO> cadastraOuAtualizarVeiculoAoUsuario(@PathVariable Long idUsuario, @RequestBody VeiculoDTO dto) {

        VeiculoDTO veiculo = service.cadastrarOuAtualizarVeiculoAoUsuario(idUsuario, dto);

        return ResponseEntity.status(201).body(veiculo);

    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<VeiculoDTO>listarVeiculoPorUsuario(@PathVariable Long id) {

        VeiculoDTO veiculo = service.listarVeiculoUsuario(id);

        return ResponseEntity.status(201).body(veiculo);

    }





}
