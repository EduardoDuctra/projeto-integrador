package br.projeto_integrador.aplicativo.backend.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.*;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import br.projeto_integrador.aplicativo.backend.security.TokenServiceJWT;
import br.projeto_integrador.aplicativo.backend.security.TokenServiceGoogle;
import br.projeto_integrador.aplicativo.backend.services.AutenticacaoService;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenServiceJWT tokenServiceJWT;
    private final TokenServiceGoogle tokenServiceGoogle;
    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AuthenticationManager authenticationManager,
                                  TokenServiceJWT tokenServiceJWT, UsuarioRepository usuarioRepository, TokenServiceGoogle tokenServiceGoogle, UsuarioService usuarioService, AutenticacaoService autenticacaoService) {
        this.authenticationManager = authenticationManager;
        this.tokenServiceJWT = tokenServiceJWT;
        this.autenticacaoService = autenticacaoService;
        this.tokenServiceGoogle = tokenServiceGoogle;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid DadosAutenticacaoDTO dados) {

        System.out.println("Tentativa de login com email: " + dados.email());

        var authToken = new UsernamePasswordAuthenticationToken(
                dados.email(),
                dados.senha()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            User user = (User) authentication.getPrincipal();

            String token = tokenServiceJWT.gerarToken(user);

            return ResponseEntity.ok(new DadosTokenJWTDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Usuário ou senha incorretos");
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody TokenGoogleDTO dto) {

        // valida dados do google
        GoogleUserInfoDTO googleInfo = tokenServiceGoogle.validarToken(dto.token());

        // busca ou cria usuário no banco
        Usuario usuario = usuarioService.usuarioGoogle(googleInfo);

        //  Cria UserDetails
        UserDetails userDetails = autenticacaoService.loadUserByUsername(usuario.getEmail());

        // Gera JWT
        tokenServiceJWT.gerarToken(userDetails);

        // retorna token + dados do usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail()
        );

        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestBody EsqueciSenhaDTO dto) {
        usuarioService.enviarCodigoRecuperacao(dto.email());
        return ResponseEntity.ok("Código enviado");
    }


    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody NovaSenhaDTO dto) {

        usuarioService.redefinirSenha(dto.email(), dto.codigo(), dto.novaSenha());

        return ResponseEntity.ok("Senha atualizada");
    }

}