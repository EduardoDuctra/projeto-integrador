package br.projeto_integrador.aplicativo.backend.model.entity;


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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conector")
    private Long idConector;

    @Column(name = "tipo_conector")
    @Enumerated(EnumType.STRING)
    private TipoCarregador tipo;

    @Column(name = "em_uso")
    private boolean emUso;

    @Column(name = "soc_recarga_atual")
    private double socRecarga;

    @Column(name = "data_ultima_atualizacao")
    private LocalDateTime dataAtualizacao;


    @ManyToOne
    @JoinColumn(name = "id_carregador", nullable = false)
    private Carregador carregador;



}
