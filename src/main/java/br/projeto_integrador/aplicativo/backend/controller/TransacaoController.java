package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarValorMaximoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoFinanceiraDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoFinanceiraService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {


    private final TransacaoFinanceiraService transacaoFinanceiraService;
    private final SecurityUtils securityUtils;


    public TransacaoController(TransacaoFinanceiraService transacaoFinanceiraService, SecurityUtils securityUtils) {
        this.transacaoFinanceiraService = transacaoFinanceiraService;

        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<TransacaoFinanceiraDTO> criarTransacao(HttpServletRequest request,
                                                                 @RequestBody TransacaoFinanceiraDTO transacaoDTO) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        TransacaoFinanceiraDTO dtoSalvo = transacaoFinanceiraService.criarTransacao(id, transacaoDTO);
        return ResponseEntity.status(201).body(dtoSalvo);

    }

    //atualizar transacao -> valor maximo
    //patch atualiza só uma parte
    @PatchMapping("/{idTransacao}/valor-maximo")
    public ResponseEntity<AtualizarValorMaximoDTO> atualizarValorMaximoTransacao(HttpServletRequest request,
                                                                                 @PathVariable Long idTransacao,
                                                                                 @RequestBody AtualizarValorMaximoDTO dto) {

        Long idUsuario = securityUtils.getUsuarioPeloIdToken(request);
        AtualizarValorMaximoDTO atualizado = transacaoFinanceiraService.atualizarTransacao(idUsuario, idTransacao, dto.valorMaximo());

        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/listar-transacoes-credito")
    public ResponseEntity<List<TransacaoFinanceiraDTO>>listarTransacoes(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<TransacaoFinanceiraDTO> transacoes = this.transacaoFinanceiraService.listarPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }
        return ResponseEntity.status(200).body(transacoes);
    }


}
