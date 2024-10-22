package pl.bilskik.citifier.ctfcore.login;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotNull
    private String login;
    @NotNull
    private String password;
}
