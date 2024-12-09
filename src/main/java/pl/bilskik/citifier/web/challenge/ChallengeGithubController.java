package pl.bilskik.citifier.web.challenge;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.web.github.GithubDataInputDTO;

import static pl.bilskik.citifier.web.challenge.ChallengeConstraints.*;

@Controller
@RequiredArgsConstructor
public class ChallengeGithubController {

    private final ChallengeService challengeService;

    @GetMapping("/challenge-repo-link")
    public String githubChallengeLinkPage(
            GithubDataInputDTO modelGithubDataInputDTO,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        GithubDataInputDTO githubDataInputDTO = new GithubDataInputDTO();
        if(session.getAttribute(GITHUB_DATA) != null) {
            githubDataInputDTO = (GithubDataInputDTO) session.getAttribute(GITHUB_DATA);
        }

        String error = (String) model.getAttribute(CLONE_ERROR);
        if(!githubDataInputDTO.getIsRepoClonedSuccessfully() && error != null && !error.isEmpty()) {
            modelGithubDataInputDTO.setGithubLink(githubDataInputDTO.getGithubLink());
            bindingResult.rejectValue("githubLink","error.githubLink", error);
            return "challenge/github/challenge-github-repo";
        }

        model.addAttribute("githubDataInputDTO", githubDataInputDTO);
        return "challenge/github/challenge-github-repo";
    }

    @PostMapping("/challenge-creation")
    public String createChallenge(Model model, HttpSession session) {
        ChallengeInputDTO challengeInputDTO = (ChallengeInputDTO) session.getAttribute(CHALLENGE);
        GithubDataInputDTO githubDataInputDTO = (GithubDataInputDTO) session.getAttribute(GITHUB_DATA);

        try {
            challengeService.createNewChallenge(challengeInputDTO, githubDataInputDTO);
        } catch(Exception e) {
            model.addAttribute("githubDataInputDTO", githubDataInputDTO);
            return "challenge/github/challenge-github-repo";
        }
        session.removeAttribute(CHALLENGE);
        session.removeAttribute(GITHUB_DATA);

        return "redirect:/challenge-list";
    }
}
