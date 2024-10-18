package pl.bilskik.citifier.ctfcreator.challenge.github;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeCreationService;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeInputDTO;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.*;

@Controller
@RequiredArgsConstructor
public class ChallengeGithubController {

    private final ChallengeCreationService challengeCreationService;

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
            return "ctfcreator/challenge/github/challenge-github-repo";
        }

        model.addAttribute("githubDataInputDTO", githubDataInputDTO);
        return "ctfcreator/challenge/github/challenge-github-repo";
    }

    @PostMapping("/ctf-creator/challenge-creation")
    public String createChallenge(HttpSession session) {
        ChallengeInputDTO challengeInputDTO = (ChallengeInputDTO) session.getAttribute(CHALLENGE);
        GithubDataInputDTO githubDataInputDTO = (GithubDataInputDTO) session.getAttribute(GITHUB_DATA);

        try {
            challengeCreationService.createNewChallenge(challengeInputDTO, githubDataInputDTO);
        } catch(Exception e) {
            return "ctfcreator/challenge/github/challenge-github-repo";
        }
        session.removeAttribute(CHALLENGE);
        session.removeAttribute(GITHUB_DATA);

        return "redirect:/challenge-list";
    }
}
