package br.projeto_integrador.aplicativo.backend.model.dto;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoFinanceiraDTO(Long idTransacao,
                                     TipoTransacao tipoTransacao,
                                     BigDecimal valor,
                                     StatusTransacao statusTransacao,
                                     LocalDateTime data,
                                     Long idUsuario,
                                     Long transacaoRecarga) {
}
