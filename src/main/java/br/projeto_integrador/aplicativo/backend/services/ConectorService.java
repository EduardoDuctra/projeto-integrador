package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.entity.Conector;
import br.projeto_integrador.aplicativo.backend.repositories.ConectorRepository;
import org.springframework.stereotype.Service;

@Service
public class ConectorService {

    private final ConectorRepository conectorRepository;

    public ConectorService(ConectorRepository conectorRepository) {
        this.conectorRepository = conectorRepository;
    }

    public Conector buscarPorId(Long id) {
        return conectorRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Conector não encontrado"));
    }
}
