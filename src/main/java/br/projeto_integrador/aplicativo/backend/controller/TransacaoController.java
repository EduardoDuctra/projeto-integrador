package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.services.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {


    private final TransacaoService transacaoService;


    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;

    }

    @PostMapping
    public ResponseEntity<TransacaoDTO> criarTransacao(@RequestBody TransacaoDTO transacaoDTO) {

        TransacaoDTO dtoSalvo = transacaoService.criarTransacao(transacaoDTO);
        return ResponseEntity.status(201).body(dtoSalvo);

    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TransacaoDTO> atualizarTransacao(@PathVariable Long id,
                                                           @RequestBody TransacaoDTO dto) {

        TransacaoDTO atualizado = transacaoService.atualizarTransacao(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/listar-transacoes-usuario/{id}")
    public ResponseEntity<List<TransacaoDTO>>listarTransacoes(@PathVariable Long id) {

        List<TransacaoDTO> transacoes = this.transacaoService.listarPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }
        return ResponseEntity.status(200).body(transacoes);
    }


}
