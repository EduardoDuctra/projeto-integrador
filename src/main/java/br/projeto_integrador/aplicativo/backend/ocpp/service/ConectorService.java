package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Carregador;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.entity.Eletroposto;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.MeterValuesCompletoDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.SampledValueDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.StatusNotificationDTO;
import br.projeto_integrador.aplicativo.backend.repositories.CarregadorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import org.springframework.stereotype.Service;

@Service
public class ConectorService {

    private final CarregadorRepository carregadorRepository;
    private final ConectorRepository conectorRepository;


    public ConectorService(CarregadorRepository carregadorRepository, ConectorRepository conectorRepository) {
        this.carregadorRepository = carregadorRepository;
        this.conectorRepository = conectorRepository;
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
            conector.setEmUso(true);
        }
        else {
            conector.setEmUso(false);
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

        if (dto.tipoCarregador() != null){
            conector.setTipo(dto.tipoCarregador());
        }


        conectorRepository.save(conector);


        String response = "Conector ID: " + conector.getId() + "atualizado com sucesso";

        return response;

    }
}
