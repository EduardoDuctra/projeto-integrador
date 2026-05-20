package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.CriarUsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.SenhaAtualizarDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioResponseDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Path relacionado ao usuário")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;

    public UsuarioController(UsuarioService usuarioService, SecurityUtils securityUtils) {
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    @Operation(summary = "Criar um usuário", description = "Criar um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Email ou CPF já cadastrados")
    })
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
    @Operation(summary = "Atualizar foto do usuário", description = "Atualizar foto do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL da foto retornada com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")
                    )),
    })
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
    @Operation(summary = "Listar os usuários - DESENVOLVIMENTO", description = "Listar os usuários - DESENVOLVIMENTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    })
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
    @Operation(summary = "Retornar o usuário logado", description = "Retornar o usuário logado com base no token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))
            ),
    })
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
    @Operation(summary = "Atualizar os dados do usuário", description = "Atualizar os dados do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
    })
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(HttpServletRequest request,
                                                               @RequestBody UsuarioDTO dto) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        UsuarioResponseDTO atualizado = usuarioService.atualizarUsuario(id, dto);

        return ResponseEntity.ok(atualizado);

    }


    @PutMapping("/atualizar/senha")
    @Operation(summary = "Atualizar a senha do usuário", description = "Atualizar a senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"))
            ),
    })
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
    @Operation(summary = "Atualizar o veículo principal do usuário", description = "Atualizar o veículo principal do usuário. Recebe o ID do veículo pela URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo alterado com sucesso",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"))
            ),
            @ApiResponse(responseCode = "400", description = "Usuário ou veículo não encontrado")

    })
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
    @Operation(summary = "Desativa o perfil do usuário usuário", description = "Desativa o perfil do usuário usuário")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário desativado com sucesso"
            )


    })
    public ResponseEntity<Void> deletarUsuario(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        usuarioService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }



}
