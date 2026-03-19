package br.projeto_integrador.aplicativo.backend.infra;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger logger = LoggerFactory.getLogger(TratadorDeErros.class);

    // 404 - Não encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> tratadorErro404(NoSuchElementException ex) {
        // Retorna um JSON com a mensagem de erro
        return ResponseEntity.status(404).body(Map.of("erro", ex.getMessage()));
    }


    // 400 - Dados inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErroDadosInvalidos(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getFieldErrors();
        List<DadosErroValidacao> dados = new ArrayList<>();
        for (FieldError fe : errors) {
            dados.add(new DadosErroValidacao(fe.getField(), fe.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(dados);
    }

    // 500 - Erro interno
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErroGenerico(Exception ex) {
        logger.error("Erro interno do servidor: ", ex);
        return ResponseEntity.status(500).body("Erro interno do servidor: " + ex.getMessage());
    }


    private record DadosErroValidacao(String campo, String mensagem) { }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> tratarErroIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(RegraDeNegociosException.class)
    public ResponseEntity<Object> tratarRegraNegocio(RegraDeNegociosException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }


}