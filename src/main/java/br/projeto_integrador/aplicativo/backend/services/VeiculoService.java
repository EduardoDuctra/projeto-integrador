package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.MarcaDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ModeloDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.MarcaEntity;
import br.projeto_integrador.aplicativo.backend.model.entity.ModeloEntity;
import br.projeto_integrador.aplicativo.backend.model.entity.UsuarioEntity;
import br.projeto_integrador.aplicativo.backend.model.entity.VeiculoEntity;
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

        UsuarioEntity usuario = usuarioService.buscarPorId(idUsuario);

        if(dto.modelo() == null || dto.modelo().idModelo() == null){
            throw new RegraDeNegociosException("Modelo é obrigatório");
        }

        Long idModelo = dto.modelo().idModelo();

        ModeloEntity modelo = modeloRepository.findById(idModelo).
                orElseThrow(() -> new RegraDeNegociosException("Modelo não encontrado"));


        VeiculoEntity veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario).orElse(new VeiculoEntity());
        veiculo.setUsuario(usuario);
        veiculo.setModelo(modelo);


        VeiculoEntity salvo = veiculoRepository.save(veiculo);


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

        VeiculoEntity veiculo = veiculoRepository.findByUsuario_IdUsuario(idUsuario)
                . orElseThrow(() -> new RegraDeNegociosException("Veículo não encontrado para o usuário"));

        ModeloEntity modelo = veiculo.getModelo();
        MarcaEntity marca = modelo.getMarca();

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
