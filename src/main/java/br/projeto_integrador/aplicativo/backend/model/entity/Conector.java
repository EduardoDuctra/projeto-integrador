package br.projeto_integrador.aplicativo.backend.model.entity;


import br.projeto_integrador.aplicativo.backend.model.enums.NomeConector;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusNotification;
import br.projeto_integrador.aplicativo.backend.model.enums.TipoConector;
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
    private TipoConector tipo;

    @Column(name = "disponivel_uso")
    private boolean disponivelUso;

    @Column(name = "status_conector")
    @Enumerated(EnumType.STRING)
    private StatusNotification statusConcetor;


    @Column(name = "soc_recarga_atual")
    private double socRecarga;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataAtualizacao;


    @Column(name = "nome_conector")
    @Enumerated(EnumType.STRING)
    private NomeConector nomeConector;


    @Column(name = "valor_energia")
    private Double valorEnergia;

    @ManyToOne
    @JoinColumn(name = "id_carregador", nullable = false)
    private Carregador carregador;



}
