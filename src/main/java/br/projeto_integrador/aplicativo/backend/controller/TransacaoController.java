package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarValorMaximoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoAtivaDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoCreditoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoDebitoDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoFinanceiraService;
import br.projeto_integrador.aplicativo.backend.websocket.TransacaoSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transacao")
public class TransacaoController {


    private final TransacaoFinanceiraService transacaoFinanceiraService;
    private final SecurityUtils securityUtils;
    private final TransacaoSubject transacaoSubject;


    public TransacaoController(TransacaoFinanceiraService transacaoFinanceiraService, SecurityUtils securityUtils, TransacaoSubject transacaoSubject) {
        this.transacaoFinanceiraService = transacaoFinanceiraService;

        this.securityUtils = securityUtils;
        this.transacaoSubject = transacaoSubject;
    }

    @PostMapping
    public ResponseEntity<TransacaoCreditoDTO> criarTransacao(HttpServletRequest request,
                                                              @RequestBody TransacaoCreditoDTO transacaoDTO) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        TransacaoCreditoDTO dtoSalvo = transacaoFinanceiraService.criarTransacao(id, transacaoDTO);

//        //observer
//        transacaoSubject.notificar(id);

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
    public ResponseEntity<List<TransacaoCreditoDTO>>listarTransacoes(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<TransacaoCreditoDTO> transacoes = this.transacaoFinanceiraService.listarPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }


        return ResponseEntity.status(200).body(transacoes);
    }


    @GetMapping("/listar-transacoes-debito")
    public ResponseEntity<List<TransacaoDebitoDTO>>listarTransacoesDebito(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<TransacaoDebitoDTO> transacoes = this.transacaoFinanceiraService.listarTransacaoDebitoPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }
        return ResponseEntity.status(200).body(transacoes);
    }

    @GetMapping("/listar-transacao-ativa")
    public ResponseEntity<TransacaoAtivaDTO>listarTransacaoAtiva(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        TransacaoAtivaDTO transacaoAtiva = this.transacaoFinanceiraService.listarTransacaoAtivaPorUsuario(id);

        if(transacaoAtiva == null){
            return ResponseEntity.status(404).body(null);
        }


        return ResponseEntity.status(200).body(transacaoAtiva);
    }


}
