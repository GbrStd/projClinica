package gbrstd.clinica.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpDto {

    @NotEmpty(message = "Usuário é obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Usuário deve conter apenas letras e números")
    @Length(min = 3, max = 20, message = "Usuário deve conter entre 3 e 20 caracteres")
    private String username;

    @NotEmpty(message = "Senha é obrigatória")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s)[0-9a-zA-Z!@#$%^&*()]*$", message = "Senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número")
    @Length(min = 6, max = 20, message = "Senha deve conter entre 6 e 20 caracteres")
    private String password;

    private String confirmPassword;

}
