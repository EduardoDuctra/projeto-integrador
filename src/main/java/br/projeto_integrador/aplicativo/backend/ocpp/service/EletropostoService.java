package br.projeto_integrador.aplicativo.backend.ocpp.service;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.EletropostoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Eletroposto;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusEletroposto;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import br.projeto_integrador.aplicativo.backend.repositories.EletropostoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EletropostoService {

    private final EletropostoRepository eletropostoRepository;

    public EletropostoService(EletropostoRepository eletropostoRepository) {
        this.eletropostoRepository = eletropostoRepository;
    }


    @Transactional
    public EletropostoDTO cadastrarEletroposto(EletropostoDTO dto) {

        if(dto.idEletroposto()!=null){
            boolean existe = eletropostoRepository.existsById(dto.idEletroposto());

            if (existe) {
                throw new RegraDeNegociosException("ID do eletroposto já existe no banco");
            }

        }


        Eletroposto eletroposto = new Eletroposto();
        eletroposto.setNomeEletroposto(dto.nomeEletroposto());
        eletroposto.setCidade(dto.cidade());
        eletroposto.setUf(dto.uf());
        eletroposto.setEndereco(dto.endereco());
        eletroposto.setStatusEletroposto(StatusEletroposto.ATIVO);

        Eletroposto salvo = eletropostoRepository.save(eletroposto);

        return new EletropostoDTO(
                salvo.getIdEletroposto(),
                salvo.getNomeEletroposto(),
                salvo.getCidade(),
                salvo.getUf(),
                salvo.getEndereco(),
                salvo.getStatusEletroposto()
        );
    }

    public EletropostoDTO atualizarEletroposto(Long id, EletropostoDTO dto) {

        Eletroposto eletroposto = eletropostoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Eletroposto não encontrado"));

        if(dto.nomeEletroposto()!=null){
            eletroposto.setNomeEletroposto(dto.nomeEletroposto());

        }

        if(dto.cidade()!=null){
            eletroposto.setCidade(dto.cidade());
        }

        if(dto.uf()!=null){
            eletroposto.setUf(dto.uf());
        }

        if(dto.endereco()!=null){
            eletroposto.setEndereco(dto.endereco());
        }

        Eletroposto atualizado = eletropostoRepository.save(eletroposto);

        return new EletropostoDTO(
                atualizado.getIdEletroposto(),
                atualizado.getNomeEletroposto(),
                atualizado.getCidade(),
                atualizado.getUf(),
                atualizado.getEndereco(),
                eletroposto.getStatusEletroposto()
        );
    }

    public void desativarEletroposto(Long id) {

        Eletroposto eletroposto = eletropostoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Eletroposto não encontrado"));

        eletroposto.setStatusEletroposto(StatusEletroposto.DESATIVADO);
        eletropostoRepository.save(eletroposto);
    }


}



