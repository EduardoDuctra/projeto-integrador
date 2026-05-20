package br.projeto_integrador.aplicativo.backend.controller;


import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.mercadoPago.PagamentoService;
import br.projeto_integrador.aplicativo.backend.model.dto.*;
import br.projeto_integrador.aplicativo.backend.security.SecurityUtils;
import br.projeto_integrador.aplicativo.backend.services.TransacaoFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transacao")
@Tag(name = "Transacao", description = "Path relacionado as transações")
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
    @Operation(summary = "Criar uma transação de crédito", description = "Criar uma transação de crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Link de pagamento do mercado pago",
                    content = @Content(mediaType = "text/plain",  schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Erro ao validar transação de crédito")
    })
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
    @Operation(summary = "Atualiza o valor máximo da recarga", description = "Atualiza o valor máximo da recarga. Recebe o ID da transação pela URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor máximo atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarValorMaximoDTO.class))),
    })
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
    @Operation(summary = "Lista as transações de crédito do usuário",
            description = "Lista as transações de crédito do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com as transações",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoCreditoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada")
    })
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
    @Operation(summary = "Lista as transações de débito do usuário",
            description = "Lista as transações de débito do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com as transações",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoDebitoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada")
    })
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
    @Operation(summary = "Lista a transação ativa",
            description = "Lista a transação ativa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista a trasanção",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoAtivaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada")
    })
    public ResponseEntity<TransacaoAtivaDTO>listarTransacaoAtiva(HttpServletRequest request) {

        Long id = securityUtils.getUsuarioPeloIdToken(request);
        TransacaoAtivaDTO transacaoAtiva = this.transacaoFinanceiraService.listarTransacaoAtivaPorUsuario(id);

        if(transacaoAtiva == null){
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(transacaoAtiva);
    }


}
