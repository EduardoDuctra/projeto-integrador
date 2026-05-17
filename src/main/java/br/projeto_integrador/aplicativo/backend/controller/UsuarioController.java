package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.CriarUsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.SenhaAtualizarDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioResponseDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
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
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody CriarUsuarioDTO dto) {

       UsuarioResponseDTO usarioSalvo = usuarioService.criarUsuario(dto);
       return ResponseEntity.status(201).body(usarioSalvo);

    }

    /**
     * Recebe o arquivo foto e atualiza a foto
     * não funciona junto com o metodo de atualizar os dados -> service separado
     * @param request
     * @param foto
     * @return String
     */
    @PostMapping("/logado/foto")
    public ResponseEntity<String> uploadFoto(HttpServletRequest request,
                                             @RequestParam("foto") MultipartFile foto
    ) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String url = usuarioService.atualizarFoto(id, foto);

        return ResponseEntity.status(201).body(url);
    }


    /**
     * lista os usuarios do sistema -> APENAS DESENVOLVIMENTO
     * @return <List<UsuarioCompletoDTO>
     */
    @GetMapping("/listar-usuarios")
    public ResponseEntity<List<UsuarioDTO>>listarUsuarios(){
        List<UsuarioDTO> usuarios = this.usuarioService.listarUsuarios();

        if(usuarios.isEmpty()){
            return ResponseEntity.status(404).body(usuarios);
        }
        return ResponseEntity.status(200).body(usuarios);
    }

    /**
     * busca o usuario logado no banco de dados
     * @param request
     * @return UsuarioCompletoDTO
     */
    @GetMapping("/logado")
    public ResponseEntity<UsuarioDTO>buscarUsuarioLogado(HttpServletRequest request){


        Long id = securityUtils.getUsuarioPeloIdToken(request);

        Usuario usuario = usuarioService.buscarPorId(id);

        Long idVeiculo = null;
        String modeloVeiculo = null;

        if(usuario.getVeiculoPrincipal() != null){

            idVeiculo = usuario.getVeiculoPrincipal().getIdVeiculo();
            modeloVeiculo = usuario.getVeiculoPrincipal().getModeloCarro();
        }


        UsuarioDTO dto = new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getFotoUrl(),
                usuario.getSaldo(),
                usuario.isCadastroCompleto(),
                idVeiculo,
                modeloVeiculo);

        return ResponseEntity.status(200).body(dto);

    }


    /**
     * atualiza os dados do usuário
     * @param request
     * @param dto
     * @return UsuarioDTO
     */
    @PutMapping("/atualizar/logado")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(HttpServletRequest request,
                                                               @RequestBody UsuarioDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        UsuarioResponseDTO atualizado = usuarioService.atualizarUsuario(id, dto);

        return ResponseEntity.ok(atualizado);

    }


    @PutMapping("/atualizar/senha")
    public ResponseEntity<String> atualizarSenha(HttpServletRequest request,
                                                               @RequestBody SenhaAtualizarDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String atualizado = usuarioService.atualizarSenha(id, dto);

        return ResponseEntity.ok(atualizado);

    }

    /**
     * atualiza o veículo principal do usuário -> o que vai ser salvo na transação atual
     * usa a lista de veículos dele
     * @param request
     * @param idVeiculo
     * @return String
     */
    @PutMapping("/atualizar-veiculo/{idVeiculo}")
    public ResponseEntity<String> atualizarVeiculoPrincipal(HttpServletRequest request,
                                                            @PathVariable Long idVeiculo){

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        String resposta = usuarioService.atualizarVeiculoPrincipal(id, idVeiculo);

        return ResponseEntity.ok(resposta);
    }


    /**
     * soft delete
     * @param request
     * @return void
     */
    @DeleteMapping("/deletar/logado")
    public ResponseEntity<Void> deletarUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        usuarioService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }



}
