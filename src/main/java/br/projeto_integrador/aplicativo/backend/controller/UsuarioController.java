package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioCadastroDTO dto) {

       UsuarioDTO usarioSalvo = usuarioService.criarUsuario(dto);
       return ResponseEntity.status(201).body(usarioSalvo);

    }

    @GetMapping("/listar-usuarios")
    public ResponseEntity<List<UsuarioDTO>>listarUsuarios(){
        List<UsuarioDTO> usuarios = this.usuarioService.listarUsuarios();

        if(usuarios.isEmpty()){
            return ResponseEntity.status(404).body(usuarios);
        }
        return ResponseEntity.status(200).body(usuarios);
    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioCadastroDTO dto) {

        UsuarioDTO atualizado = usuarioService.atualizarUsuario(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {

        usuarioService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }

}
