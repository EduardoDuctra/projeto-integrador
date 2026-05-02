package br.projeto_integrador.aplicativo.backend.ocpp.controller;

import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarCarregadorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.ConectorDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.VeiculoDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.ConectorService;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conector")
public class ConcectorController {

    private final ConectorService conectorService;
    private final SecurityUtils securityUtils;
    private final TransacaoService transacaoService;


    public ConcectorController(ConectorService conectorService, SecurityUtils securityUtils, TransacaoService transacaoService) {
        this.conectorService = conectorService;
        this.securityUtils = securityUtils;
        this.transacaoService = transacaoService;
    }


    @PutMapping("/atualizar-conector")
    public ResponseEntity<String> atualizarInformacoes(@RequestBody AtualizarConectorDTO dto){

        String response = conectorService.atualizarInformacoes(dto);

        return ResponseEntity.status(200).body(response);
    }


    @GetMapping("/usado-recentemente-pelo-usuario")
    public ResponseEntity<ConectorDTO> conectorUsadoRecentemente(HttpServletRequest request){

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        ConectorDTO dto = conectorService.listarTransacaoAtivaRecentementePorUsuario(id);


        if (dto == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/disponiveis/{idCarregador}")
    public ResponseEntity<List<ConectorDTO>>conectoresDisponiveis(@PathVariable String idCarregador){

        List<ConectorDTO> lista = conectorService.conectoresPorCarregador(idCarregador);


        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(lista);
    }


}



