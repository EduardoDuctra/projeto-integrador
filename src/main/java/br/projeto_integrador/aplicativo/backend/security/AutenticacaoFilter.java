package br.projeto_integrador.aplicativo.backend.security;

import br.projeto_integrador.aplicativo.backend.services.AutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AutenticacaoFilter extends OncePerRequestFilter {

    private final TokenServiceJWT tokenServiceJWT;
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoFilter(TokenServiceJWT tokenServiceJWT, AutenticacaoService autenticacaoService) {
        this.tokenServiceJWT = tokenServiceJWT;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("PATH: " + path);

       //ignora filtro pro que vem do servidor/python
        //não tem token aqui para receber. apenas para mandar java -> python
        if (path.contains("/backend")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Filtro para autenticacao e autorização");

        String tokenJWT = recuperarToken(request);
        System.out.println("TokenJWT: " + tokenJWT);

        if (tokenJWT != null) {
            try {
                String subject = this.tokenServiceJWT.getSubject(tokenJWT);
                System.out.println("Login: " + subject);

                UserDetails userDetails = this.autenticacaoService.loadUserByUsername(subject);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (RuntimeException e) {
                System.out.println("Token inválido ou expirado: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // segue fluxo normal
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null){
            return token.replace("Bearer ", "").trim();
        }
        return null;
    }
}