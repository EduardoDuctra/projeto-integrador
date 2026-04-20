package br.projeto_integrador.aplicativo.backend.security;

import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


//recupera o id do usuario pelo Token que veio na requisição
@Component
public class SecurityUtils {

    private final TokenServiceJWT tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityUtils(TokenServiceJWT tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    public Long getUsuarioPeloIdToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization")
                .replace("Bearer ", "")
                .trim();

        String email = tokenService.getSubject(token);

        Usuario usuario = usuarioRepository.findByEmail(email);

        return usuario.getIdUsuario();
    }
}