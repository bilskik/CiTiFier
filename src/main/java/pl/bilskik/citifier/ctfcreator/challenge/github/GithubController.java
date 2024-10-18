package pl.bilskik.citifier.ctfcreator.challenge.github;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.CLONE_ERROR;
import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.GITHUB_DATA;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ctf-creator/challenge")
public class GithubController {

    private final GithubService githubService;

    @HxRequest
    @PostMapping("/github-link")
    public String loadGithubLink(
            Model model,
            @Valid GithubDataInputDTO githubDataInputDTO,
            BindingResult bindingResult,
            @RequestParam String githubLink,
            HttpSession session
    ) {
        session.setAttribute(GITHUB_DATA, githubDataInputDTO);
        try {
            githubService.clonePublicGithubRepo(githubLink);
            updateGithubInputData(session, githubLink, false);
        } catch(GithubException e) {
            bindingResult.rejectValue("githubLink","error.githubLink", e.getMessage());
            return "ctfcreator/challenge/github/challenge-github-repo";
        }
        model.addAttribute("githubInputDataDTO", githubDataInputDTO);
        return "ctfcreator/challenge/github/challenge-github-repo";
    }

    @HxRequest
    @PostMapping("/github-repo-visibility")
    public String changeGithubRepoVisibility(
            Model model,
            @RequestParam(required = false) boolean isPrivateRepo,
            @RequestParam(required = false) String githubLink
    ) {
        if(isPrivateRepo) {
            String redirectUrl = githubService.buildRedirectUrl(githubLink);
            model.addAttribute("redirectUrl", redirectUrl);
            return "ctfcreator/challenge/github/github-private-repo-button";
        }
        return "ctfcreator/challenge/github/github-public-repo-button";
    }

    @GetMapping("/github-link-redirect")
    public View redirect(
            RedirectAttributes redirectAttributes,
            @RequestParam String code,
            @RequestParam(name = "state") String url,
            HttpSession httpSession
    ) {
        try {
            githubService.clonePrivateGithubRepo(code, url);
            updateGithubInputData(httpSession, url, true);
        } catch(GithubException e) {
            updateGithubInputDataError(httpSession, url);
            redirectAttributes.addFlashAttribute(CLONE_ERROR, e.getMessage());
        }
        return new RedirectView("/challenge-repo-link");
    }

    private void updateGithubInputData(HttpSession httpSession, String githubLink, boolean isPrivateRepo) {
        GithubDataInputDTO githubDataInputDTO = (GithubDataInputDTO) httpSession.getAttribute(GITHUB_DATA);
        if(githubDataInputDTO == null) {
            githubDataInputDTO = new GithubDataInputDTO();
        }
        githubDataInputDTO.setGithubLink(githubLink);
        githubDataInputDTO.setRepoClonedSuccessfully(true);
        githubDataInputDTO.setPrivateRepo(isPrivateRepo);
        httpSession.setAttribute(GITHUB_DATA, githubDataInputDTO);
    }

    private void updateGithubInputDataError(HttpSession httpSession, String githubLink) {
        GithubDataInputDTO githubDataInputDTO = (GithubDataInputDTO) httpSession.getAttribute(GITHUB_DATA);
        if(githubDataInputDTO == null) {
            githubDataInputDTO = new GithubDataInputDTO();
        }
        githubDataInputDTO.setGithubLink(githubLink);
        githubDataInputDTO.setRepoClonedSuccessfully(false);
        httpSession.setAttribute(GITHUB_DATA, githubDataInputDTO);
    }
}
