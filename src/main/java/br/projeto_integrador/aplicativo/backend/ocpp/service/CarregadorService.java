package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Carregador;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.BootNotificationDTO;
import br.projeto_integrador.aplicativo.backend.repositories.CarregadorRepository;
import org.springframework.stereotype.Service;

@Service
public class CarregadorService {

    private final CarregadorRepository carregadorRepository;

    public CarregadorService(CarregadorRepository carregadorRepository) {
        this.carregadorRepository = carregadorRepository;
    }

    //vem direto do OCPP
    public void processarCarregador(BootNotificationDTO payload){

        String idCharger = payload.charger_id();

        if (payload.charger_id() == null) {
            throw new RegraDeNegociosException("charger_id não pode ser nulo");
        }


        Carregador carregador = carregadorRepository.findById(idCharger).orElse(null);



        //pega o carregador do banco e seta ele
        if(carregador == null){
            carregador = new Carregador();
            carregador.setIdCarregador(idCharger);
        }


        if (payload.charge_point_serial_number() != null) {
            carregador.setNumeroSerie(payload.charge_point_serial_number());
        }

        if (payload.charge_point_vendor() != null) {
            carregador.setNomeMarca(payload.charge_point_vendor());
        }

        if (payload.charge_point_model() != null) {
            carregador.setNomeModelo(payload.charge_point_model());
        }

        if (payload.firmware_version() != null) {
            carregador.setFirmwareVersion(payload.firmware_version());
        }


        try {
            carregadorRepository.save(carregador);
        } catch (Exception e) {
            throw new RegraDeNegociosException("Erro ao salvar carregador no banco de dados");
        }

        System.out.println("Carregador salvo: " + idCharger);


    }


    //endpoint para atualizar manualmente
    public String atualizarInformacoes(AtualizarCarregadorDTO dto) {

        if(dto.idCarregador() == null){
            throw new RegraDeNegociosException("Id do carregador nulo");
        }

        Carregador carregador = carregadorRepository.findById(dto.idCarregador())
                .orElseThrow(() -> new RegraDeNegociosException("Carregador não encontrado"));

        if (dto.potenciaCorrenteAlternada() != null){
            carregador.setPotenciaCorrenteAlternada(dto.potenciaCorrenteAlternada());
        }

        if (dto.potenciaCorrenteContinua() != null){
            carregador.setPotenciaCorrenteContinua(dto.potenciaCorrenteContinua());
        }

        if(dto.cidade() != null){
            carregador.setCidade(dto.cidade());
        }

        if (dto.endereco() != null){
            carregador.setEndereco(dto.endereco());
        }



        carregadorRepository.save(carregador);


        String response = "Carregador ID: " + carregador.getIdCarregador() + "atualizado com sucesso";

        return response;

    }
}
