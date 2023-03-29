package gbrstd.clinica.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotEmpty(message = "Endereço é obrigatório")
    private String logradouro;

    @Column(nullable = false)
    private int numero;

    @Column
    @NotEmpty(message = "Bairro é obrigatório")
    private String bairro;

    @Column
    @NotEmpty(message = "Cidade é obrigatória")
    private String cidade;

    @Column
    @NotEmpty(message = "Estado é obrigatório")
    private String estado;

    @Column
    @NotEmpty(message = "País é obrigatório")
    private String pais;

    @Column
    @NotEmpty(message = "CEP é obrigatório")
    private String cep;

    public Endereco(String logradouro, int numero, String bairro, String cidade, String estado, String pais, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.cep = cep;
    }

    public String buildAddress() {
        return String.format("%s, %d, %s, %s, %s, %s, %s", logradouro, numero, bairro, cidade, estado, pais, cep);
    }

}
