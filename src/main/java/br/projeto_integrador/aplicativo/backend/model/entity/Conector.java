package br.projeto_integrador.aplicativo.backend.model.entity;


import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoCarregador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "conector")
@Getter
@Setter
@NoArgsConstructor
public class Conector {

    //id no banco
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ID do conector dentro do carregador (ex Voltta 15 - conector 1) + (ex Voltta 16 - conector 1)
    @Column(name = "connector_id")
    private Integer connectorIdNoCarregador;

    @Column(name = "tipo_conector")
    @Enumerated(EnumType.STRING)
    private TipoCarregador tipo;

    @Column(name = "em_uso")
    private boolean emUso;

    @Column(name = "status_conector")
    @Enumerated(EnumType.STRING)
    private StatusNotification statusConcetor;


    @Column(name = "soc_recarga_atual")
    private double socRecarga;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataAtualizacao;


    @ManyToOne
    @JoinColumn(name = "id_carregador", nullable = false)
    private Carregador carregador;



}
