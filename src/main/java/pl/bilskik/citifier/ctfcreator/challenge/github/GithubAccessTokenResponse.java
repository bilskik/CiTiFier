package pl.bilskik.citifier.ctfcreator.challenge.github;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GithubAccessTokenResponse {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;
    private String token_type;
}
