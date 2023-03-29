package gbrstd.clinica.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "clinica_seq",
        sequenceName = "clinica_seq",
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
public class Clinica implements Serializable {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinica_seq")
    @Id
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Nome é obrigatório")
    @Length(min = 3, max = 50, message = "Nome deve conter entre 3 e 50 caracteres")
    private String nome;

    @Column(nullable = false)
    @NotEmpty(message = "Telefone é obrigatório")
    @Length(min = 8, max = 20, message = "Telefone deve conter entre 8 e 20 caracteres")
    private String telefone;

    @Column(nullable = false)
    @NotEmpty(message = "Email é obrigatório")
    @Length(min = 8, max = 50, message = "Email deve conter entre 8 e 50 caracteres")
    private String email;

    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "clinica_endereco",
            joinColumns = @JoinColumn(name = "clinica_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    )
    private @Valid Endereco endereco;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>(0);

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<Especialista> especialistas = new ArrayList<>(0);

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<Paciente> pacientes = new ArrayList<>(0);

    public Clinica(String nome, String telefone, String email, Especialidade especialidade, Endereco endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.especialidade = especialidade;
        this.endereco = endereco;
    }

}
