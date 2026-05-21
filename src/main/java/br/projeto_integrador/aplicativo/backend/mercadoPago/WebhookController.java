package br.projeto_integrador.aplicativo.backend.mercadoPago;


import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/webhook")
@Tag(name = "Webhook", description = "Path relacionado ao Webhook - MercadoPago")
public class WebhookController {

    @Value("${api.mercado.pago.token}")
    private String accessToken;



    private final PagamentoService pagamentoService;


    public WebhookController(PagamentoService pagamentoService) {

        this.pagamentoService = pagamentoService;
    }


    /**
     * nessa url que o webhook avisa meu backend
     * tenho que configurar no mercado pago essa url
     * @param webhookDTO
     * @return String
     */
    @PostMapping
    @Operation(summary = "Recebe um webhook", description = "Recebe um webhook quando o pagamento é confirmado e processa o pagamento no BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processado com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> receberWebhook(@RequestBody WebhookDTO webhookDTO) {

        System.out.println("WEBHOOK CHEGOU!");

        String confirmacao = null;

        try {

            confirmacao = pagamentoService.processarPagamento(webhookDTO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraDeNegociosException("Erro ao processar WEBHOOK");
        }

        return ResponseEntity.ok(confirmacao);
    }

}
