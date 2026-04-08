package br.projeto_integrador.aplicativo.backend.model.entity;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusCarregador;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusEletroposto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eletroposto")
@Getter
@Setter
@NoArgsConstructor
public class Eletroposto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_eletroposto")
    private Long idEletroposto;

    @Column(name = "nome_eletroposto")
    private String nomeEletroposto;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "uf")
    private String uf;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "status_eletroposto")
    @Enumerated(EnumType.STRING)
    private StatusEletroposto statusEletroposto;


}
