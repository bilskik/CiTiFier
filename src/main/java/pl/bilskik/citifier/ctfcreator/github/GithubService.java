package pl.bilskik.citifier.ctfcreator.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestClient;
import pl.bilskik.citifier.ctfcreator.filemanager.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubService {
    private final static List<String> DOCKER_COMPOSE = Arrays.asList("docker-compose.yml", "docker-compose.yaml", "compose.yaml", "compose.yml");
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

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    private final GithubUrlValidator validator;
    private final FileManager fileManager;

    public String clonePublicGithubRepo(String url) throws GithubException {
        return validateAndCloneRepo(url, null, false);
    }

    public String clonePrivateGithubRepo(String code, String url) {
        String accessToken = retrieveUserAccessToken(code)
                .orElseThrow(() -> new GithubException(GITHUB_PRIVATE_REPO_ACCESS_ERROR));
        return validateAndCloneRepo(url, accessToken, true);
    }

    private String validateAndCloneRepo(String url, String accessToken, boolean isPrivate) {
        validator.validateGithubLink(url);
        String relativePath = fileManager.buildRelativeClonePathRepo(url);
        String filepath = buildFilePath(relativePath);

        log.info("I am cloning repo: {}", url);

        try {
            CloneCommand clone = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(filepath));

            if(isPrivate && accessToken != null) {
                clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(accessToken, ""));
            }

            clone.call();
            log.info("Succesfully cloned repo: {}", url);
        } catch(Exception e) {
            log.info("I can't clone repo: {}, reason: {}", url, e.getMessage());
            throw new GithubException(isPrivate ? GITHUB_PRIVATE_REPO_CLONE_ERROR : GITHUB_PUBLIC_REPO_ERROR);
        }
        validateDockerComposeInRepo(filepath);

        return relativePath;
    }

    private String buildFilePath(String relativeFilePath) {
        return baseFilePath + File.separator + relativeFilePath;
    }

    public String buildRedirectUrl(String url) {
        return AUTHORIZE_URI + "?client_id=" + CLIENT_ID + "&state=" + url;
    }

    private Optional<String> retrieveUserAccessToken(String code) {
        String uri = buildAccessTokenUri(code);
        log.info("Retrieving user access token");

        RestClient restClient = RestClient.create();
        ResponseEntity<GithubAccessTokenResponse> response = restClient.post()
                .uri(uri)
                .header("Accept", "application/json")
                .body("")
                .retrieve()
                .toEntity(GithubAccessTokenResponse.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            log.info("Retrieved user access token succesfully!");
            return Optional.ofNullable(response.getBody()).map(GithubAccessTokenResponse::getAccess_token);
        }

        log.error("Failed to retrieve access token. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
        return Optional.empty();
    }

    private String buildAccessTokenUri(String code) {
        return String.format("%s?client_id=%s&client_secret=%s&code=%s", ACCESS_TOKEN_URI, CLIENT_ID, CLIENT_SECRET, code);
    }

    private void validateDockerComposeInRepo(String filepath) {
        log.info("Validating Docker Compose file in repo: {}", filepath);
        boolean isDockerComposeAvailable = containsDockerCompose(filepath);
        if(!isDockerComposeAvailable) {
            fileManager.deleteDirAndRepo(filepath);
            throw new GithubException(GITHUB_DOCKER_COMPOSE_ERROR);
        }
        log.info("Docker compose file found");
    }

    private boolean containsDockerCompose(String filepath) {
        try(Git git = Git.open(new File(filepath))) {
            Repository repository = git.getRepository();
            RevWalk revWalk = new RevWalk(repository);
            TreeWalk treeWalk = new TreeWalk(repository);

            ObjectId lastCommitId = repository.resolve("HEAD");
            RevCommit lastCommit = revWalk.parseCommit(lastCommitId);
            treeWalk.addTree(lastCommit.getTree());
            treeWalk.setRecursive(false);
            while(treeWalk.next()) {
                if(DOCKER_COMPOSE.contains(treeWalk.getPathString())) {
                    return true;
                }
            }

        } catch(IOException e) {
            log.error("Error while checking for Docker Compose file in repo: {}", e.getMessage());
           return false;
        }
        return false;
    }

}
