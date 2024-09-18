package pl.bilskik.citifier.ctfcreator.challenge.github;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.File;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final static String ACCESS_TOKEN_URI = "https://github.com/login/oauth/access_token";
    private final static String AUTHORIZE_URI = "https://github.com/login/oauth/authorize";
    private final static String GITHUB_PUBLIC_REPO_ERROR = "Nie można pobrać repozytorium. Sprawdź, czy link jest poprawny i czy repozytorium nie jest prywatne";
    private final static String GITHUB_PRIVATE_REPO_ACCESS_ERROR = "Nie można uzyskać dostępu! Sprawdź poprawność danych!";
    private final static String GITHUB_PRIVATE_REPO_CLONE_ERROR = "Nie można pobrać repozytorium. Sprawdź czy link jest poprawny!";

    @Value("${oauth2.github.client-id}")
    private String CLIENT_ID;

    @Value("${oauth2.github.client-secret}")
    private String CLIENT_SECRET;

    private final GithubUrlProcessor processor;

    public void clonePublicGithubRepo(String url) throws GithubException {
        processor.validateGithubLink(url);
        String filepath = processor.buildClonePath(url);
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(filepath))
                    .call();
        } catch(Exception e) {
            throw new GithubException(GITHUB_PUBLIC_REPO_ERROR);

        }
    }

    public String buildRedirectUrl(String url) {
        return AUTHORIZE_URI + "?client_id=" + CLIENT_ID + "&state=" + url;
    }


    public void clonePrivateGithubRepo(String code, String url) {
        processor.validateGithubLink(url);
        String accessToken = retrieveUserAccessToken(code);
        String filepath = processor.buildClonePath(url);

        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(filepath))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(accessToken, ""))
                    .call();
        } catch(GitAPIException e) {
            throw new GithubException(GITHUB_PRIVATE_REPO_CLONE_ERROR);
        }
    }

    private String retrieveUserAccessToken(String code) {
        String uri = buildAccessTokenUri(code);

        RestClient restClient = RestClient.create();
        ResponseEntity<GithubAccessTokenResponse> response = restClient.post()
                .uri(uri)
                .header("Accept", "application/json")
                .body("")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new GithubException(GITHUB_PRIVATE_REPO_ACCESS_ERROR);
                })
                .onStatus(HttpStatusCode::is5xxServerError, ((req, res) -> {
                    throw new GithubException(GITHUB_PRIVATE_REPO_ACCESS_ERROR);
                }))
                .toEntity(GithubAccessTokenResponse.class);

        return response.getBody() != null ? response.getBody().getAccess_token() : null;
    }

    private String buildAccessTokenUri(String code) {
        return ACCESS_TOKEN_URI + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + code;
    }

}
