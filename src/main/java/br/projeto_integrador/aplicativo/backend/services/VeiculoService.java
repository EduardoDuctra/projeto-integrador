package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.MarcaDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ModeloDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Marca;
import br.projeto_integrador.aplicativo.backend.model.entity.Modelo;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.entity.Veiculo;
import br.projeto_integrador.aplicativo.backend.repositories.ModeloRepository;
import br.projeto_integrador.aplicativo.backend.repositories.VeiculoRepository;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {

    private final UsuarioService usuarioService;
    private final ModeloRepository modeloRepository;
    private final VeiculoRepository veiculoRepository;


    public VeiculoService(UsuarioService usuarioService, ModeloRepository modeloRepository, VeiculoRepository veiculoRepository) {
        this.usuarioService = usuarioService;
        this.modeloRepository = modeloRepository;
        this.veiculoRepository = veiculoRepository;
    }


    public VeiculoDTO cadastrarOuAtualizarVeiculoAoUsuario(Long idUsuario, VeiculoDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if(dto.modelo() == null || dto.modelo().idModelo() == null){
            throw new RegraDeNegociosException("Modelo é obrigatório");
        }

        Long idModelo = dto.modelo().idModelo();

        Modelo modelo = modeloRepository.findById(idModelo).
                orElseThrow(() -> new RegraDeNegociosException("Modelo não encontrado"));


        Veiculo veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario).orElse(new Veiculo());
        veiculo.setUsuario(usuario);
        veiculo.setModelo(modelo);


        Veiculo salvo = veiculoRepository.save(veiculo);


        MarcaDTO marcaDTO = new MarcaDTO(
                modelo.getMarca().getIdMarca(),
                modelo.getMarca().getNomeMarca(),
                modelo.getMarca().getPais());

        ModeloDTO modeloDTO = new ModeloDTO(
                idModelo,
                modelo.getModelo(),
                marcaDTO);

        return new VeiculoDTO(
                salvo.getIdVeiculo(),
                modeloDTO);
    }


    public VeiculoDTO listarVeiculoUsuario(Long idUsuario) {

        usuarioService.buscarPorId(idUsuario);

        Veiculo veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario)
                . orElseThrow(() -> new RegraDeNegociosException("Veículo não encontrado para o usuário"));

        Modelo modelo = veiculo.getModelo();
        Marca marca = modelo.getMarca();

        MarcaDTO marcaDTO = new MarcaDTO(
                marca.getIdMarca(),
                marca.getNomeMarca(),
                marca.getPais()
        );

        ModeloDTO modeloDTO = new ModeloDTO(
                modelo.getIdModelo(),
                modelo.getModelo(),
                marcaDTO);

        return new VeiculoDTO(
                veiculo.getIdVeiculo(),
                modeloDTO
        );



    }
}
