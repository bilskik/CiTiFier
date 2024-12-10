package pl.bilskik.citifier.web.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
