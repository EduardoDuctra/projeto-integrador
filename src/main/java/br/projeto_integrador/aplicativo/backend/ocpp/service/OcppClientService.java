package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStartDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.RemoteStopDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.UnlockConnectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.security.OcppTokenService;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OcppClientService {

    @Value("${ocpp.api.base-url}")
    private String URL_SERVER_OCPP;

    private final RestTemplate restTemplate;
    private final OcppTokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final ConectorRepository conectorRepository;
    private final TransacaoRepository transacaoRepository;



    public OcppClientService(RestTemplate restTemplate, OcppTokenService tokenService, UsuarioRepository usuarioRepository, ConectorRepository conectorRepository, TransacaoRepository transacaoRepository) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.conectorRepository = conectorRepository;
        this.transacaoRepository = transacaoRepository;
    }


    public String iniciarRecarga(Long idUsuario, RemoteStartDTO remoteStartDTO) {

        String url = URL_SERVER_OCPP + "/remotestart";

        System.out.println("OCPPService[" + remoteStartDTO.chargerId() + "-" + remoteStartDTO.connectorId() + "] --> remotestart");


        //associar a transação ao usuário e conectores
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Conector conector = conectorRepository.findByCarregador_IdCarregadorAndConnectorIdNoCarregador(
                        remoteStartDTO.chargerId(), remoteStartDTO.connectorId())
                .orElseThrow(() -> new RuntimeException("Conector não encontrado"));




        System.out.println("Criando nova transação");

        Transacao transacao = new Transacao();
        transacao.setUsuario(usuario);
        transacao.setConector(conector);
        transacao.setStatusTransacao(StatusTransacao.Preparing);
        transacao.setTipoTransacao(TipoTransacao.DEBITO);
        transacao.setDataInicio(LocalDateTime.now());
        transacao.setValorEnergia(BigDecimal.valueOf(conector.getValorEnergia()));

        transacaoRepository.save(transacao);



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