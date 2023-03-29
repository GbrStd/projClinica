package gbrstd.clinica.model;

import gbrstd.clinica.validator.DateRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consulta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Descrição é obrigatório")
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "Data é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateRange(message = "Data é inválida", max = "9999-01-01", minNow = true)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    @NotNull(message = "Paciente é obrigatório")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "especialista_id", nullable = false)
    @NotNull(message = "Especialista é obrigatório")
    private Especialista especialista;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private @Valid List<Exame> exames = new ArrayList<>(0);

}
