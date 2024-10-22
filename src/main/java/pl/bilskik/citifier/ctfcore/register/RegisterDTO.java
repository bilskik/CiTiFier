package pl.bilskik.citifier.ctfcore.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NotNull
public class RegisterDTO {

    @NotBlank(message = "Login nie może być pusty!")
    private String login;
    @NotBlank(message = "Hasło nie może być puste!")
    private String password;
}
