package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.security.OcppTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OcppClientService {

    @Value("${ocpp.api.base-url}")
    private String URL_SERVER_OCPP;

    private final RestTemplate restTemplate;
    private final OcppTokenService tokenService;



    public OcppClientService(RestTemplate restTemplate, OcppTokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }


    public String iniciarRecarga(RemoteStartDTO remoteStartDTO) {

        String url = URL_SERVER_OCPP + "/remotestart";

        System.out.println("OCPPService[" + remoteStartDTO.chargerId() + "-" + remoteStartDTO.connectorId() + "] --> remotestart");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = tokenService.gerarToken();
        System.out.println("TOKEN GERADO: " + token);
        headers.set("Authorization", "Bearer " + token);


        HttpEntity<RemoteStartDTO> request = new HttpEntity<>(remoteStartDTO, headers);
        try {
            return  restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RegraDeNegociosException("Erro ao publicar em remotestart");
        }

    }

    public String pararRecarga(RemoteStopDTO remoteStopDTO) {

        String url = URL_SERVER_OCPP + "/remotestop";

        System.out.println("OCPPService[" + remoteStopDTO.chargerId() + "-" + remoteStopDTO.transactionId() + "] --> remotestop");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = tokenService.gerarToken();
        System.out.println("TOKEN GERADO: " + token);
        headers.set("Authorization", "Bearer " + token);


        HttpEntity<RemoteStopDTO> request = new HttpEntity<>(remoteStopDTO, headers);
        try {
            return  restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            throw new RegraDeNegociosException("Erro ao publicar em remotestop");

        }
    }

    public String desbloquearConector(UnlockConnectorDTO unlockConnectorDTO) {

        String url = URL_SERVER_OCPP + "/unlockconnector";

        System.out.println("OCPPService[" + unlockConnectorDTO.chargerId() + "-" + unlockConnectorDTO.connectorId() + "] --> unlockconnector");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = tokenService.gerarToken();
        headers.set("Authorization", "Bearer " + token);


        HttpEntity<UnlockConnectorDTO> request = new HttpEntity<>(unlockConnectorDTO, headers);
        try {
            return  restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            throw new RegraDeNegociosException("Erro ao publicar em unlockconnector");

        }

    }
}