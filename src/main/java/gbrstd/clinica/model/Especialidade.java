package gbrstd.clinica.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Especialidade {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "A descrição da especialidade é obrigatória")
    private String descricao;

    @OneToMany(mappedBy = "especialidade")
    private List<Clinica> clinicas = new ArrayList<>(0);

    public Especialidade(String descricao) {
        this.descricao = descricao;
    }

}
