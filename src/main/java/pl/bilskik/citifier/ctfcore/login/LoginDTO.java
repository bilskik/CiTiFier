package pl.bilskik.citifier.ctfcore.login;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotNull
    private String tournamentCode;
    @NotNull
    private String login;
    @NotNull
    private String password;
}
