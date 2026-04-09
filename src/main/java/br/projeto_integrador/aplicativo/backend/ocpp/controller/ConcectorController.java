package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarConectorDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.ConectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conector")
public class ConcectorController {

    private final ConectorService conectorService;

    public ConcectorController(ConectorService conectorService) {
        this.conectorService = conectorService;
    }


    @PutMapping("/atualizar-conector")
    public ResponseEntity<String> atualizarInformacoes(@RequestBody AtualizarConectorDTO dto){

        String response = conectorService.atualizarInformacoes(dto);

        return ResponseEntity.status(200).body(response);
    }


}
