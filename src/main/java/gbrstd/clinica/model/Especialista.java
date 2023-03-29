package gbrstd.clinica.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Especialista extends Pessoa {

    @ManyToOne
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @OneToMany(mappedBy = "especialista", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>(0);

}
