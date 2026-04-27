package br.projeto_integrador.aplicativo.backend.security;

import br.projeto_integrador.aplicativo.backend.model.dto.GoogleUserInfoDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class TokenServiceGoogle {

    public GoogleUserInfoDTO validarToken(String token) {

        try {
            String url = "https://www.googleapis.com/oauth2/v3/userinfo";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();

            if (body == null || body.get("email") == null) {
                throw new RuntimeException("Token Google inválido");
            }

            String email = (String) body.get("email");
            String nome = (String) body.get("name");

            return new GoogleUserInfoDTO(email, nome);

        } catch (Exception e) {
            throw new RuntimeException("Token Google inválido ou expirado");
        }
    }
}