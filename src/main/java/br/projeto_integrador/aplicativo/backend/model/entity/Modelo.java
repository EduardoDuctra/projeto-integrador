package br.projeto_integrador.aplicativo.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelo")
@Getter
@Setter
@NoArgsConstructor
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long idModelo;

    @Column(name = "modelo_carro", nullable = false)
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;
}