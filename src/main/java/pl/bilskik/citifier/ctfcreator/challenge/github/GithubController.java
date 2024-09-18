package pl.bilskik.citifier.ctfcreator.challenge.github;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeController;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ctf-creator/challenge")
public class GithubController {
    private static final String SUCCESS_REPO_CLONED = "Repozytorium zostało sklonowane poprawnie!";
    private final GithubService githubService;

    @HxRequest
    @PostMapping("/github-link")
    public ResponseEntity<String> loadGithubLink(@Nullable String githubLink) {
        try {
            githubService.clonePublicGithubRepo(githubLink);
        } catch(GithubException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(SUCCESS_REPO_CLONED, HttpStatus.OK);
    }

    @HxRequest
    @PostMapping("/github-repo-visibility")
    public String changeGithubRepoVisibility(
            Model model,
            @RequestParam(required = false) boolean githubRepoPrivateVisibility,
            @RequestParam(required = false) String githubLink
    ) {
        if(githubRepoPrivateVisibility) {
            String redirectUrl = githubService.buildRedirectUrl(githubLink);
            model.addAttribute("redirectUrl", redirectUrl);
            return "ctfcreator/challenge/github/github-private-repo-button";
        }
        return "ctfcreator/challenge/github/github-public-repo-button";
    }

    @GetMapping("/github-link-redirect")
    public View redirect(RedirectAttributes redirectAttributes, @RequestParam String code, @RequestParam(name = "state") String url) {
        String info;
        String githubLinkState = "OK";
        try {
            githubService.clonePrivateGithubRepo(code, url);
            info = SUCCESS_REPO_CLONED;
        } catch(GithubException e) {
            info = e.getMessage();
            githubLinkState = "ERROR";
        }
        redirectAttributes.addFlashAttribute("githubLinkState", githubLinkState);
        redirectAttributes.addFlashAttribute("githubLinkInfo", info);
        return new RedirectView("/challenge");
    }
}
