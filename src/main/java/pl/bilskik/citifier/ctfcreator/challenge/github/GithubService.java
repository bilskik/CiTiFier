package pl.bilskik.citifier.ctfcreator.challenge.github;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml");
    private final static String ACCESS_TOKEN_URI = "https://github.com/login/oauth/access_token";
    private final static String AUTHORIZE_URI = "https://github.com/login/oauth/authorize";
    private final static String GITHUB_PUBLIC_REPO_ERROR = "Nie można pobrać repozytorium. Sprawdź, czy link jest poprawny i czy repozytorium nie jest prywatne";
    private final static String GITHUB_PRIVATE_REPO_ACCESS_ERROR = "Nie można uzyskać dostępu! Sprawdź poprawność danych!";
    private final static String GITHUB_PRIVATE_REPO_CLONE_ERROR = "Nie można pobrać repozytorium. Sprawdź czy link jest poprawny!";
    private final static String GITHUB_DOCKER_COMPOSE_ERROR = "Repozytorium nie posiada pliku docker compose!";

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
        boolean isDockerCompose = containsDockerCompose(filepath);
        System.out.println("Docker Compose " + isDockerCompose);
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
        } catch(Exception e) {
            throw new GithubException(GITHUB_PRIVATE_REPO_CLONE_ERROR);
        }

        boolean isDockerCompose = containsDockerCompose(filepath);
        System.out.println("Docker Compose " + isDockerCompose);
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


    private boolean containsDockerCompose(String filepath) { //to be changed
        try {
            Git git = Git.open(new File(filepath));
            Repository repository = git.getRepository();
            ObjectId lastCommitId = repository.resolve("HEAD");
            RevWalk revWalk = new RevWalk(repository);
            RevCommit lastCommit = revWalk.parseCommit(lastCommitId);
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(lastCommit.getTree());
            treeWalk.setRecursive(false);
            while(treeWalk.next()) {
                if(DOCKER_COMPOSE.contains(treeWalk.getPathString())) {
                    return true;
                }
            }

        } catch(IOException e) {
            throw new GithubException(GITHUB_DOCKER_COMPOSE_ERROR);
        }

        return false;
    }

}
