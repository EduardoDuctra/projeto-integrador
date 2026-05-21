package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.*;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.security.TokenServiceGoogle;
import br.projeto_integrador.aplicativo.backend.security.TokenServiceJWT;
import br.projeto_integrador.aplicativo.backend.services.AutenticacaoService;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Path relacionado a autenticação")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenServiceJWT tokenServiceJWT;
    private final TokenServiceGoogle tokenServiceGoogle;
    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AuthenticationManager authenticationManager,
                                  TokenServiceJWT tokenServiceJWT, TokenServiceGoogle tokenServiceGoogle,
                                  UsuarioService usuarioService, AutenticacaoService autenticacaoService) {
        this.authenticationManager = authenticationManager;
        this.tokenServiceJWT = tokenServiceJWT;
        this.autenticacaoService = autenticacaoService;
        this.tokenServiceGoogle = tokenServiceGoogle;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Realizar o login", description = "Realiza o login com email e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosTokenJWTDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha incorretos", content = @Content)
    })
    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid DadosAutenticacaoDTO dados) {

        System.out.println("Tentativa de login com email: " + dados.email());

        var authToken = new UsernamePasswordAuthenticationToken(
                dados.email(),
                dados.senha());

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            User user = (User) authentication.getPrincipal();

            String token = tokenServiceJWT.gerarToken(user);

            return ResponseEntity.ok(new DadosTokenJWTDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Usuário ou senha incorretos");
        }
    }

    /**
     * validação com o google. Fornece apenas Nome e Email. Demais dados tem que ser
     * preenchidos manualmente pelo usuário
     * @param dto
     * @return DadosTokenJWTDTO
     */
    @PostMapping("/google")
    @Operation(summary = "Realizar o login via Google", description = "Realiza o login via Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login via Google realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosTokenJWTDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token Google inválido", content = @Content)
    })
    public ResponseEntity<?> loginGoogle(@RequestBody TokenGoogleDTO dto) {

        // valida dados do google
        GoogleUserInfoDTO googleInfo = tokenServiceGoogle.validarToken(dto.token());

        // busca ou cria usuário no banco
        Usuario usuario = usuarioService.usuarioGoogle(googleInfo);

        //  Cria UserDetails
        UserDetails userDetails = autenticacaoService.loadUserByUsername(usuario.getEmail());

        // Gera JWT
        String token = tokenServiceJWT.gerarToken(userDetails);


        return ResponseEntity.ok(new DadosTokenJWTDTO(token));

    }

    /**
     * apenas envia o código de recuperação para o email
     * @param dto
     * @return String
     */
    @PostMapping("/esqueci-senha")
    @Operation(summary = "Envia o código de recuperação para o email", description = "Envia o código de recuperação para o email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código enviado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Usuário não encontrado", content = @Content)
    })
    public ResponseEntity<String> esqueciSenha(@RequestBody EsqueciSenhaDTO dto) {
        usuarioService.enviarCodigoRecuperacao(dto.email());
        return ResponseEntity.ok("Código enviado");
    }


    /**
     * pega o codigo de recuperação + email + nova senha e atualiza
     * @param dto
     * @return String
     */
    @PostMapping("/redefinir-senha")
    @Operation(summary = "Envia o código de recuperação para o email", description = "Envia o código de recuperação para o email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Código não encontrado", content = @Content)
    })
    public ResponseEntity<String> redefinirSenha(@RequestBody NovaSenhaDTO dto) {

        usuarioService.redefinirSenha(dto.email(), dto.codigo(), dto.novaSenha());

        return ResponseEntity.ok("Senha atualizada");
    }

}