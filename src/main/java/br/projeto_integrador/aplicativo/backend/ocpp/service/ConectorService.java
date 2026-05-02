package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Carregador;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Transacao;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValuesCompletoDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.SampledValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.StatusNotificationDTO;
import br.projeto_integrador.aplicativo.backend.repositories.CarregadorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.TransacaoRepository;
import br.projeto_integrador.aplicativo.backend.services.UsuarioService;
import br.projeto_integrador.aplicativo.backend.websocket.TransacaoSubject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConectorService {

    private final CarregadorRepository carregadorRepository;
    private final ConectorRepository conectorRepository;
    private final UsuarioService usuarioService;
    private final TransacaoRepository transacaoRepository;
    private final TransacaoSubject transacaoSubject;



    public ConectorService(CarregadorRepository carregadorRepository, ConectorRepository conectorRepository, UsuarioService usuarioService, TransacaoRepository transacaoRepository, TransacaoSubject transacaoSubject) {
        this.carregadorRepository = carregadorRepository;
        this.conectorRepository = conectorRepository;
        this.usuarioService = usuarioService;
        this.transacaoRepository = transacaoRepository;
        this.transacaoSubject = transacaoSubject;
    }




    public void processarConector(StatusNotificationDTO payload){

        String idCharger = payload.charger_id();

        if (payload.charger_id() == null) {
            throw new RegraDeNegociosException("charger_id não pode ser nulo");
        }

        Carregador carregador = carregadorRepository.findById(idCharger)
                .orElseThrow(() -> new RegraDeNegociosException("Carregador não encontrado"));

        Integer idConector = payload.connector_id();

        if(payload.connector_id() == null){
            throw new RegraDeNegociosException("connector_id não pode ser nulo");
        }

        Conector conector = conectorRepository.findByCarregador_IdCarregadorAndConnectorIdNoCarregador(idCharger, idConector)
                .orElse(null);


        //se for null -> seta os dados
        if (conector == null) {
            conector = new Conector();
            conector.setCarregador(carregador);
            conector.setConnectorIdNoCarregador(idConector);
        }

        if (payload.status() != null) {
            conector.setStatusConcetor(payload.status());
        }

        if(payload.status() != StatusNotification.Available){
            conector.setDisponivelUso(false);
        }
        else {
            conector.setDisponivelUso(true);
            conector.setSocRecarga(0.0);
        }


        try {
            conectorRepository.save(conector);
        } catch (Exception e) {
            throw new RegraDeNegociosException("Erro ao salvar conector no banco de dados");
        }

        System.out.println("conector salvo: " + idCharger);

    }

    //ficar atualizando o soc atual e ultima atualizacao
    public void processarMeterValues(MeterValuesCompletoDTO payload) {

        String chargerId = payload.charger_id();
        Integer connectorId = payload.connector_id();

        Conector conector = conectorRepository
                .findByCarregador_IdCarregadorAndConnectorIdNoCarregador(chargerId, connectorId)
                .orElseThrow(() -> new RuntimeException("Conector não encontrado"));

        for (MeterValueDTO meter : payload.meter_value()) {

            for (SampledValueDTO sample : meter.sampled_value()) {

                if ("SoC".equals(sample.measurand())) {
                    conector.setSocRecarga(sample.value());
                }

            }

            if (meter.timestamp() != null) {
                conector.setDataAtualizacao(meter.timestamp().toLocalDateTime());
            }
        }

        conectorRepository.save(conector);
    }


    public String atualizarInformacoes(AtualizarConectorDTO dto) {

        if(dto.charger_id() == null){
            throw new RegraDeNegociosException("Id do carregador nulo");
        }

        if(dto.connector_id() == null){
            throw new RegraDeNegociosException("Id do conector nulo");
        }

        Conector conector = conectorRepository
                .findByCarregador_IdCarregadorAndConnectorIdNoCarregador(dto.charger_id(), dto.connector_id())
                .orElseThrow(() -> new RuntimeException("Conector não encontrado"));

        if (dto.tipoConector() != null){
            conector.setTipo(dto.tipoConector());
            conector.setValorEnergia(dto.tipoConector().getValorKwh());

        }

        if(dto.nomeConector() != null){
            conector.setNomeConector(dto.nomeConector());
        }


        conectorRepository.save(conector);


        String response = "Conector ID: " + conector.getId() + "atualizado com sucesso";

        return response;

    }


    //retorna TRUE se tiver um CC ativo
    public boolean existeCCCarregando(String idCarregador) {

        List<Conector> conectores = conectorRepository
                .findByCarregador_IdCarregador(idCarregador);

        for (Conector conector : conectores) {

            if (conector.getTipo() == TipoConector.CC &&
                    conector.getStatusConcetor() == StatusNotification.Charging) {

                return true;
            }
        }

        return false;
    }



    public void atualizarDisponivelUsoCC(String idCarregador) {

        List<Conector> conectores = conectorRepository
                .findByCarregador_IdCarregador(idCarregador);

        boolean existeCCCarregando = false;

        //  verifica se existe CC carregando
        for (Conector conector : conectores) {
            if (conector.getTipo() == TipoConector.CC &&
                    conector.getStatusConcetor() == StatusNotification.Charging) {

                existeCCCarregando = true;
                break;
            }
        }


        for (Conector conector : conectores) {

            if (conector.getTipo() == TipoConector.CC) {

                if (conector.getStatusConcetor() == StatusNotification.Charging) {
                    // está sendo usado -> indisponível
                    conector.setDisponivelUso(false);

                } else if (existeCCCarregando) {
                    // outro CC está sendo usado -> indisponível
                    conector.setDisponivelUso(false);

                } else {

                    conector.setDisponivelUso(true);
                }


                conectorRepository.save(conector);
            }
        }

        //notifier
        transacaoSubject.notificarCarregador(idCarregador);

    }


    public List<ConectorDTO>conectoresPorCarregador(String idCarregador){


        List<Conector> conectores = conectorRepository.findByCarregador_IdCarregador(idCarregador);

        List<ConectorDTO> conectoresDisponiveis = new ArrayList<ConectorDTO>();

        for(Conector c : conectores){
            if(c.isDisponivelUso()){

                ConectorDTO dto = new ConectorDTO(
                        c.getId(),
                        c.getConnectorIdNoCarregador(),
                        c.getTipo(),
                        true,
                        c.getNomeConector(),
                        c.getCarregador().getIdCarregador()
                );
                conectoresDisponiveis.add(dto);
            }
        }

        return conectoresDisponiveis;

    }



    //lista Retorna o conector da transação ativa
    public ConectorDTO listarTransacaoAtivaRecentementePorUsuario(Long id) {

        Usuario usuario = usuarioService.buscarPorId(id);

        Optional<Transacao> transacaoRecente = transacaoRepository.findTopByUsuarioAndDataFimIsNotNullOrderByDataFimDesc(usuario);

        if (transacaoRecente.isEmpty()){
            return null;
        }

        Transacao t = transacaoRecente.get();

        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        LocalDateTime cincoMinutos = agora.minusMinutes(5);


        if (t.getDataFim().isAfter(cincoMinutos)) {

            Conector c = t.getConector();

            return new ConectorDTO(
                    c.getId(),
                    c.getConnectorIdNoCarregador(),
                    c.getTipo(),
                    c.isDisponivelUso(),
                    c.getNomeConector(),
                    c.getCarregador().getIdCarregador()
            );
        }

        return null;
    }


}
