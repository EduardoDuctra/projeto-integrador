package br.projeto_integrador.aplicativo.backend.ocpp.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.EletropostoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.ocpp.service.EletropostoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eletroposto")
public class EletropostoController {

    private final EletropostoService eletropostoService;

    public EletropostoController(EletropostoService eletropostoService) {
        this.eletropostoService = eletropostoService;
    }


    /**
     *
     * cadastro de eletroposto manula -> o carregador manda sinal. o Eletroposto não
     * associar o carregador no eletroposto depois
     * @param dto
     * @return
     */

    @PostMapping("/cadastrar")
    public ResponseEntity<EletropostoDTO> cadastrarEletroposto(@RequestBody EletropostoDTO dto) {

        EletropostoDTO eletropostoSalvo = eletropostoService.cadastrarEletroposto(dto);
        return ResponseEntity.status(201).body(eletropostoSalvo);

    }

    @PutMapping("/atualizar-eletroposto/{id}")
    public ResponseEntity<EletropostoDTO> atualizarEletroposto( @PathVariable Long id,
                                                                @RequestBody EletropostoDTO dto){

        EletropostoDTO atualizado = eletropostoService.atualizarEletroposto(id, dto);

        return ResponseEntity.ok(atualizado);


    }


    @DeleteMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarEletroposto(@PathVariable Long id) {

        eletropostoService.desativarEletroposto(id);

        return ResponseEntity.noContent().build();
    }
}


