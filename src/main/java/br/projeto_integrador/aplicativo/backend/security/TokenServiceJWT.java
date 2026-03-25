package br.projeto_integrador.aplicativo.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceJWT {

    private final String chave = "projeto_integrador";
    private final String emissor = "api-app-pagamentos";

    // gera o token
    public String gerarToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chave);

            return JWT.create()
                    .withIssuer(emissor)
                    .withSubject(user.getUsername())
                    .withClaim("role",
                            user.getAuthorities().stream()
                                    .findFirst()
                                    .map(auth -> auth.getAuthority())
                                    .orElse("USER")
                    )
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    // valida token e retorna o usuário (email)
    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chave);

            return JWT.require(algorithm)
                    .withIssuer(emissor)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }
}