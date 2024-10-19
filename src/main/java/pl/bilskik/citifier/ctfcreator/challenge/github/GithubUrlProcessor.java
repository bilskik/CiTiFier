package pl.bilskik.citifier.ctfcreator.challenge.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubUrlProcessor {

    private final static String PROTOCOL = "https:";
    private final static String DOMAIN = "github.com";
    private final static int MINIMUM_URL_LENGTH = 5;
    private final static String GITHUB_LINK_FORMAT = "https://github.com/{użytkownik}/{repozytorium}";

    @Value("${repo.base-file-path}")
    private String baseFilePath;

    public String buildClonePath(String url) {
        String[] tokens = splitUrl(url);
        return baseFilePath + "\\" + tokens[tokens.length - 1];
    }

    public void validateGithubLink(String url) {
        if(url == null || url.isEmpty()) {
            throw new GithubException("Link jest pusty! Format: " + GITHUB_LINK_FORMAT);
        }

        String[] tokens = splitUrl(url);
        if(tokens.length < MINIMUM_URL_LENGTH) {
            throw new GithubException("Nieprawidłowy link! Format: " + GITHUB_LINK_FORMAT);
        }

        if(!tokens[0].equals(PROTOCOL)) {
            throw new GithubException("Nieprawidłowy protkół! Format: " + GITHUB_LINK_FORMAT);
        }

        if(!tokens[2].equals(DOMAIN)) {
            throw new GithubException("Nieprawidłowa domena! Format: " + GITHUB_LINK_FORMAT);
        }
    }

    private String[] splitUrl(String url) {
        return  url.split("/");
    }
}
