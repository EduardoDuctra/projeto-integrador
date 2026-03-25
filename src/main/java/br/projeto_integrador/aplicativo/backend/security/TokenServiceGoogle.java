package br.projeto_integrador.aplicativo.backend.security;

import br.projeto_integrador.aplicativo.backend.model.dto.GoogleUserInfoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TokenServiceGoogle {

    public GoogleUserInfoDTO validarToken(String token) {

        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.get("email") == null) {
                throw new RuntimeException("Token Google inválido");
            }

            String email = (String) response.get("email");
            String nome = (String) response.get("name");

            return new GoogleUserInfoDTO(email, nome);

        } catch (Exception e) {
            throw new RuntimeException("Token Google inválido ou expirado");
        }
    }
}