package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.entity.Veiculo;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import br.projeto_integrador.aplicativo.backend.repositories.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private final UsuarioService usuarioService;
    private final VeiculoRepository veiculoRepository;


    public VeiculoService(UsuarioService usuarioService, VeiculoRepository veiculoRepository) {
        this.usuarioService = usuarioService;
        this.veiculoRepository = veiculoRepository;
    }


    public VeiculoDTO cadastrarVeiculoAoUsuario(Long idUsuario, VeiculoDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if(dto.modeloCarro() == null){
            throw new RegraDeNegociosException("Modelo é obrigatório");
        }



        Veiculo veiculo = new Veiculo();

        veiculo.setUsuario(usuario);
        veiculo.setModeloCarro(dto.modeloCarro());
        veiculo.setNomeMarca(dto.nomeMarca());


        Veiculo salvo = veiculoRepository.save(veiculo);



        return new VeiculoDTO(
                salvo.getIdVeiculo(),
                salvo.getModeloCarro(),
                salvo.getNomeMarca());
    }


    public List<VeiculoDTO> listarVeiculoUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        List<Veiculo> veiculos = veiculoRepository.findAllByUsuario_IdUsuario(idUsuario);
        List<VeiculoDTO>veiculoDTOS = new ArrayList<VeiculoDTO>();

        for(Veiculo veiculo : veiculos){
            veiculoDTOS.add(new VeiculoDTO(
                            veiculo.getIdVeiculo(),
                            veiculo.getModeloCarro(),
                            veiculo.getNomeMarca())
                    );
        }

        if(veiculoDTOS.isEmpty()){
            throw new RegraDeNegociosException("Nenhum veículo encontrado");
        }

        return veiculoDTOS;

    }

    public void deletarVeiculo(Long idVeiculo) {

            Optional<Veiculo> veiculo = veiculoRepository.findById(idVeiculo);

            if(veiculo.isEmpty()){

                throw new RegraDeNegociosException("Veículo não encontrado");

            }
            veiculoRepository.deleteById(idVeiculo);


    }
}
