package gbrstd.clinica.model;

import gbrstd.clinica.validator.DateRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // create a table for each subclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Nome é obrigatório")
    private String nome;

    @Column(nullable = false)
    @NotEmpty(message = "Telefone é obrigatório")
    @Length(min = 8, max = 20, message = "Telefone deve conter entre 8 e 20 caracteres")
    private String telefone;

    @Column(nullable = false)
    @NotEmpty(message = "Email é obrigatório")
    private String email;

    @Column(name = "data_nasci", nullable = false)
    @NotNull(message = "Data de nascimento é obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateRange(message = "Data de nascimento inválida", min = "1900-01-01", maxNow = true)
    private Date dataNasci;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "pessoa_endereco",
            joinColumns = @JoinColumn(name = "pessoa_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    )
    private @Valid Endereco endereco;

}
