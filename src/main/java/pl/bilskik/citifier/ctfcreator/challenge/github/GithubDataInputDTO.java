package pl.bilskik.citifier.ctfcreator.challenge.github;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GithubDataInputDTO {
    @Pattern(regexp = "https:\\/\\/github\\.com\\/([^\\/]+)\\/([^\\/]+)", message = "Link do repozytorium powinien być w formacie: https://github.com/{użytkownik}/{repozytorium}")
    private String githubLink;
    private boolean isPrivateRepo;
    private boolean isRepoClonedSuccessfully;

    public boolean getIsPrivateRepo() {
        return isPrivateRepo;
    }

    public boolean getIsRepoClonedSuccessfully() {
        return isRepoClonedSuccessfully;
    }
}
