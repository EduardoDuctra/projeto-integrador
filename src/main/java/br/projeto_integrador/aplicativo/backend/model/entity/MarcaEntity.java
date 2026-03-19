package br.projeto_integrador.aplicativo.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "marca")
@Getter @Setter
@NoArgsConstructor
public class MarcaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;

    @Column(name = "nome_marca", nullable = false)
    private String nomeMarca;

    @Column(name = "pais", nullable = false)
    private String pais;

    @OneToMany(mappedBy = "marca")
    private List<ModeloEntity> modelos;
}
