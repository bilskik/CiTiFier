package pl.bilskik.citifier.ctfcreator.challenge.github;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.CHALLENGE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ctf-creator/challenge")
public class GithubController {

    private static final String SUCCESS_REPO_CLONED = "Repozytorium zostało sklonowane poprawnie!";
    private static final String SUCCESS = "OK";
    private static final String ERROR = "ERROR";

    private final GithubService githubService;

    @HxRequest
    @PostMapping("/github-link")
    public View loadGithubLink(
            ChallengeDTO challengeDTO,
            @RequestParam String githubLink,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        session.setAttribute(CHALLENGE, challengeDTO);
        String info;
        String githubLinkState = SUCCESS;
        try {
            githubService.clonePublicGithubRepo(githubLink);
            updateChallengeRepoClonedStatus(session);
            info = SUCCESS_REPO_CLONED;
        } catch(GithubException e) {
            info = e.getMessage();
            githubLinkState = ERROR;
        }
        redirectAttributes.addFlashAttribute("githubLinkState", githubLinkState);
        redirectAttributes.addFlashAttribute("githubLinkInfo", info);
        return new RedirectView("/challenge");
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

    @PostMapping("/save-in-session")
    @ResponseBody
    public void saveInSession(ChallengeDTO challengeDTO, HttpSession httpSession) {
        ChallengeDTO sessionChallengeDTO = (ChallengeDTO) httpSession.getAttribute(CHALLENGE);
        if(sessionChallengeDTO == null || !sessionChallengeDTO.getIsRepoClonedSuccessfully()) {
            httpSession.setAttribute(CHALLENGE, challengeDTO);
        }
    }

    @GetMapping("/github-link-redirect")
    public View redirect(
            RedirectAttributes redirectAttributes,
            @RequestParam String code,
            @RequestParam(name = "state") String url,
            HttpSession httpSession
    ) {
        String info;
        String githubLinkState = SUCCESS;
        try {
            githubService.clonePrivateGithubRepo(code, url);
            info = SUCCESS_REPO_CLONED;
            updateChallengeRepoClonedStatus(httpSession);
        } catch(GithubException e) {
            info = e.getMessage();
            githubLinkState = ERROR;
        }
        redirectAttributes.addFlashAttribute("githubLinkState", githubLinkState);
        redirectAttributes.addFlashAttribute("githubLinkInfo", info);
        return new RedirectView("/challenge");
    }

    private void updateChallengeRepoClonedStatus(HttpSession httpSession) {
        ChallengeDTO challengeDTO = (ChallengeDTO) httpSession.getAttribute(CHALLENGE);
        if(challengeDTO == null) {
            challengeDTO = new ChallengeDTO();
        }
        challengeDTO.setRepoClonedSuccessfully(true);
        httpSession.setAttribute(CHALLENGE, challengeDTO);
    }
}
