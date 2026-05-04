package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.mercadoPago.PagamentoService;
import br.projeto_integrador.aplicativo.backend.model.dto.AtualizarValorMaximoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoAtivaDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoCreditoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.TransacaoDebitoDTO;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoFinanceiraService;
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
    private final PagamentoService pagamentoService;



    public TransacaoController(TransacaoFinanceiraService transacaoFinanceiraService,
                               SecurityUtils securityUtils, PagamentoService pagamentoService) {
        this.transacaoFinanceiraService = transacaoFinanceiraService;

        this.securityUtils = securityUtils;

        this.pagamentoService = pagamentoService;
    }

    /**
     * usuário solicita um valor de crédito -> chama o service de pagamento
     * retorna o link para efetuar o pagamento
     * @param request
     * @param transacaoDTO
     * @return String
     */
    @PostMapping
    public ResponseEntity<String> criarTransacao(HttpServletRequest request,
                                                              @RequestBody TransacaoCreditoDTO transacaoDTO) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        TransacaoCreditoDTO dtoSalvo = transacaoFinanceiraService.criarTransacao(id, transacaoDTO);

        String idTransacao = dtoSalvo.id().toString();
        double valorTransacao = dtoSalvo.valorRecarga().doubleValue();
        String link;

        try {

            link = pagamentoService.criarPagamento(valorTransacao, idTransacao);

        } catch (Exception e) {

            throw new RegraDeNegociosException("Erro ao validar transação de crédito");

        }

        return ResponseEntity.ok(link);

    }

    /**
     * atualizar transacao -> valor maximo
     * patch atualiza só uma parte
     * @param request
     * @param idTransacao
     * @param dto
     * @return AtualizarValorMaximoDTO
     */
    @PatchMapping("/{idTransacao}/valor-maximo")
    public ResponseEntity<AtualizarValorMaximoDTO> atualizarValorMaximoTransacao(HttpServletRequest request,
                                                                                 @PathVariable Long idTransacao,
                                                                                 @RequestBody AtualizarValorMaximoDTO dto) {

        Long idUsuario = securityUtils.getUsuarioPeloIdToken(request);
        AtualizarValorMaximoDTO atualizado = transacaoFinanceiraService.atualizarTransacao(idUsuario, idTransacao, dto.valorMaximo());

        return ResponseEntity.ok(atualizado);
    }

    /**
     * Retorna as transações de crétido do usuário
     * @param request
     * @return <List<TransacaoCreditoDTO>
     */
    @GetMapping("/listar-transacoes-credito")
    public ResponseEntity<List<TransacaoCreditoDTO>>listarTransacoes(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<TransacaoCreditoDTO> transacoes = this.transacaoFinanceiraService.listarPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }


        return ResponseEntity.status(200).body(transacoes);
    }


    /**
     * Retorna as transações de débito do usuário
     * @param request
     * @return <List<TransacaoDebitoDTO>
     */
    @GetMapping("/listar-transacoes-debito")
    public ResponseEntity<List<TransacaoDebitoDTO>>listarTransacoesDebito(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        List<TransacaoDebitoDTO> transacoes = this.transacaoFinanceiraService.listarTransacaoDebitoPorUsuario(id);

        if(transacoes.isEmpty()){
            return ResponseEntity.status(404).body(transacoes);
        }
        return ResponseEntity.status(200).body(transacoes);
    }

    /**
     * lista a transação ativa do usuário -> o carregamento ativo dele
     * @param request
     * @return
     */
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
