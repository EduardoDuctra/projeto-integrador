package br.projeto_integrador.aplicativo.backend.model.entity;


import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carregador")
@Getter
@Setter
@NoArgsConstructor
public class Carregador {


    @Id
    @Column(name = "id_carregador")
    private String idCarregador;


    @Column(name = "numero_serie")
    private String numeroSerie;

    @Column(name = "nome_marca")
    private String nomeMarca;

    @Column(name = "nome_modelo")
    private String nomeModelo;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "potencia_cc")
    private double potenciaCorrenteContinua;

    @Column(name = "potencia_ca")
    private double potenciaCorrenteAlternada;

    @Column(name = "status_carregador")
    @Enumerated(EnumType.STRING)
    private StatusCarregador statusCarregador;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "endereco")
    private String endereco;


}
