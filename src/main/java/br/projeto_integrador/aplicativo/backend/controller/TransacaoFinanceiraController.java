package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoFinanceiraDTO;
import br.projeto_integrador.aplicativo.backend.services.TransacaoFinanceiraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao-financeira")
public class TransacaoFinanceiraController {


    private final TransacaoFinanceiraService transacaoFinanceiraService;


    public TransacaoFinanceiraController(TransacaoFinanceiraService transacaoFinanceiraService) {
        this.transacaoFinanceiraService = transacaoFinanceiraService;

    }

    @PostMapping
    public ResponseEntity<TransacaoFinanceiraDTO> criarTransacao(@RequestBody TransacaoFinanceiraDTO transacaoDTO) {

        TransacaoFinanceiraDTO dtoSalvo = transacaoFinanceiraService.criarTransacao(transacaoDTO);
        return ResponseEntity.status(201).body(dtoSalvo);

    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TransacaoFinanceiraDTO> atualizarTransacao(@PathVariable Long id,
                                                           @RequestBody TransacaoFinanceiraDTO dto) {

        TransacaoFinanceiraDTO atualizado = transacaoFinanceiraService.atualizarTransacao(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/listar-transacoes-financeiras-usuario/{id}")
    public ResponseEntity<List<TransacaoFinanceiraDTO>>listarTransacoes(@PathVariable Long id) {

        List<TransacaoFinanceiraDTO> transacoes = this.transacaoFinanceiraService.listarPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }
        return ResponseEntity.status(200).body(transacoes);
    }


}
