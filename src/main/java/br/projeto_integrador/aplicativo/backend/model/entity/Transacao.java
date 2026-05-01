package br.projeto_integrador.aplicativo.backend.model.entity;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusTransacao;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //vem do OCPP
    @Column(name = "id_transacao")
    private Long idTransacao;

    @Column(name = "status_transacao")
    @Enumerated(EnumType.STRING)
    private StatusTransacao statusTransacao;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @Column(name = "valor_recarga")
    private BigDecimal valorRecarga;

    @Column(name = "valor_maximo")
    private BigDecimal valorMaximo;

    @Column(name = "valor_energia")
    private BigDecimal valorEnergia;

    @Column(name = "meter_start")
    private Double meterStart;

    @Column(name = "meter_stop")
    private Double meterStop;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "energia_consumida")
    private Double energiaConsumida;

    @Column(name = "soc_atual")
    private Double socAtual;

    @Column(name = "modelo_veiculo")
    private String modeloVeiculo;

    @ManyToOne
    @JoinColumn(name = "id_conector")
    private Conector conector;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}