package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.entity.Veiculo;
import br.projeto_integrador.aplicativo.backend.repositories.VeiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {

    private final UsuarioService usuarioService;
    private final VeiculoRepository veiculoRepository;


    public VeiculoService(UsuarioService usuarioService, VeiculoRepository veiculoRepository) {
        this.usuarioService = usuarioService;
        this.veiculoRepository = veiculoRepository;
    }


    public VeiculoDTO cadastrarOuAtualizarVeiculoAoUsuario(Long idUsuario, VeiculoDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if(dto.modeloCarro() == null){
            throw new RegraDeNegociosException("Modelo é obrigatório");
        }



        Veiculo veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario).orElse(new Veiculo());
        veiculo.setUsuario(usuario);
        veiculo.setModeloCarro(dto.modeloCarro());
        veiculo.setNomeMarca(dto.nomeMarca());


        Veiculo salvo = veiculoRepository.save(veiculo);



        return new VeiculoDTO(
                salvo.getIdVeiculo(),
                salvo.getModeloCarro(),
                salvo.getNomeMarca());
    }


    public VeiculoDTO listarVeiculoUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        Veiculo veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario)
                . orElseThrow(() -> new RegraDeNegociosException("Veículo não encontrado para o usuário"));



        return new VeiculoDTO(
                veiculo.getIdVeiculo(),
                veiculo.getModeloCarro(),
                veiculo.getNomeMarca()
        );



    }
}
