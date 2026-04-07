package br.projeto_integrador.aplicativo.backend.model.entity;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao_recarga")
@Getter
@Setter
@NoArgsConstructor
public class TransacaoRecarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transacao")
    private Long idTransacaoRecarga;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_transacao", nullable = false)
    private StatusTransacao statusTransacao;

    @Column(name = "meter_start", nullable = false)
    private double meterStart;

    @Column(name = "meter_stop", nullable = false)
    private double meterStop;


    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "energia_consumida", nullable = false)
    private double energiaConsumida;

    @Column(name = "soc_atual", nullable = false)
    private double socAtual;

    @Column(name = "data_soc", nullable = false)
    private LocalDateTime dataSoc;


    @ManyToOne
    @JoinColumn(name = "id_conector", nullable = false)
    private Conector conector;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}