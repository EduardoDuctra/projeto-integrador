package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OcppClientService {

    @Value("${ocpp.api.base-url}")
    private String baseURL;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔥 coloca seu token aqui
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiamF2YS1jbGllbnQifQ.ZOzg573Zz2Usv9uvuvxa02P-7_hRFRE8aPtm6dpW3EI";

    public String testarConexao() {
        String url = baseURL + "/remotestart";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN); // ✅ ESSENCIAL

        String jsonBody = """
            {
                "charger_id": "charger01",
                "connector_id": 1
            }
        """;

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            String response = restTemplate.postForObject(url, request, String.class);
            return "Resposta: " + response;
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public String iniciarRecarga(RemoteStartDTO remoteStartDTO) {
    }

    public String pararRecarga(RemoteStopDTO remoteStopDTO) {
    }

    public String desbloquearConector(UnlockConnectorDTO unlockConnectorDTO) {
    }
}