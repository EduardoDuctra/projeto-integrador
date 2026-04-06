package br.projeto_integrador.aplicativo.backend.ocpp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OcppTokenService {

    @Value("${ocpp.api.secret}")
    private String secret;

    @Value("${ocpp.api.issuer}")
    private String issuer;

    public String gerarToken() {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer(issuer)
                .withClaim("service", "ocpp-client")
                .withIssuedAt(new Date())
                .sign(algorithm);
    }
}