package br.projeto_integrador.aplicativo.backend.model.entity;

import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuario")
@Setter @Getter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome_usuario", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "senha", nullable = true)
    private String senha;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusUsuario status;

    @Column(name = "cadastro_completo")
    private boolean cadastroCompleto;

    @OneToMany(mappedBy = "usuario")
    private List<Veiculo> veiculos;
}
