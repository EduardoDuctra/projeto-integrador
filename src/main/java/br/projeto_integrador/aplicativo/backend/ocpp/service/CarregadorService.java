package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.CarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Carregador;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import br.projeto_integrador.aplicativo.backend.ocpp.dto.BootNotificationDTO;
import br.projeto_integrador.aplicativo.backend.repositories.CarregadorRepository;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import br.projeto_integrador.aplicativo.backend.websocket.TransacaoSubject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarregadorService {

    private final CarregadorRepository carregadorRepository;
    private final ConectorRepository conectorRepository;
    private final TransacaoSubject transacaoSubject;


    public CarregadorService(CarregadorRepository carregadorRepository, ConectorRepository conectorRepository, TransacaoSubject transacaoSubject) {
        this.carregadorRepository = carregadorRepository;
        this.conectorRepository = conectorRepository;

        this.transacaoSubject = transacaoSubject;
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
            carregador.setStatusCarregador(StatusCarregador.DISPONIVEL);
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

    public void atualizarStatusCarregadores(String idCarregador){


        List<Conector> conectores = conectorRepository.findByCarregador_IdCarregador(idCarregador);

        boolean temDisponivel = false;

        for(Conector c : conectores){

            if(c.isDisponivelUso()){

                temDisponivel = true;
                break;

            }
        }

        //atualizar status carregadores
        Carregador carregador = carregadorRepository.findById(idCarregador)
                .orElseThrow(() -> new RegraDeNegociosException("Carregador não encontrado"));

        if(!temDisponivel){

            carregador.setStatusCarregador(StatusCarregador.OCUPADO);

            carregadorRepository.save(carregador);

        } else{

            carregador.setStatusCarregador(StatusCarregador.DISPONIVEL);

            carregadorRepository.save(carregador);

        }

        //notifier
        transacaoSubject.notificarTodosCarregadores();

    }

    public List<CarregadorDTO> listarCarregadoresDisponiveis(){


        List<Carregador> carregadores = carregadorRepository.findAll();

        List<CarregadorDTO> carregadoresDisponiveis = new ArrayList<>();

        for(Carregador c : carregadores){

            if(c.getStatusCarregador() == StatusCarregador.DISPONIVEL){

                CarregadorDTO dto = new CarregadorDTO(
                        c.getIdCarregador(),
                        c.getStatusCarregador(),
                        c.getCidade()
                );

                carregadoresDisponiveis.add(dto);
            }
        }

        return carregadoresDisponiveis;

    }

}
