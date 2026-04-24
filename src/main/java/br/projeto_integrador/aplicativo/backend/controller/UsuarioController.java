package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;

    public UsuarioController(UsuarioService usuarioService, SecurityUtils securityUtils) {
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioCadastroDTO dto) {

       UsuarioDTO usarioSalvo = usuarioService.criarUsuario(dto);
       return ResponseEntity.status(201).body(usarioSalvo);

    }

    @PostMapping("/logado/foto")
    public ResponseEntity<String> uploadFoto(HttpServletRequest request,
                                             @RequestParam("foto") MultipartFile foto
    ) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String url = usuarioService.atualizarFoto(id, foto);

        return ResponseEntity.status(201).body(url);
    }


    @GetMapping("/listar-usuarios")
    public ResponseEntity<List<UsuarioCadastroDTO>>listarUsuarios(){
        List<UsuarioCadastroDTO> usuarios = this.usuarioService.listarUsuarios();

        if(usuarios.isEmpty()){
            return ResponseEntity.status(404).body(usuarios);
        }
        return ResponseEntity.status(200).body(usuarios);
    }


    @PutMapping("/atualizar/logado")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(HttpServletRequest request,
                                                       @RequestBody UsuarioCadastroDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        UsuarioDTO atualizado = usuarioService.atualizarUsuario(id, dto);

        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/deletar/logado")
    public ResponseEntity<Void> deletarUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        usuarioService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }



}
