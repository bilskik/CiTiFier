package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.CHALLENGE;


@Controller
@RequiredArgsConstructor
public class ChallengeController {

    private final static List<String> flagGenerationMethod;

    static {
        flagGenerationMethod = FlagGenerationMethod.convertToList();
    }

    private final ChallengeCreationService challengeCreationService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("flagGenerationMethods", flagGenerationMethod);
    }

    @GetMapping(path = "/challenge")
    public String challengePage(Model model, HttpSession httpSession) {
        ChallengeDTO challengeDTO = new ChallengeDTO();
        if(httpSession.getAttribute(CHALLENGE) != null) {
            challengeDTO = (ChallengeDTO) httpSession.getAttribute(CHALLENGE);
        }
        model.addAttribute("challengeDTO", challengeDTO);
        return "ctfcreator/challenge/challenge";
    }

    @HxRequest
    @PostMapping(value = "/ctf-creator/challenge")
    public String submitChallenge(
            @ModelAttribute @Valid ChallengeDTO challengeDTO,
            BindingResult result,
            Model model,
            HttpSession httpSession
    ) {
        if(challengeDTO != null) {
            updateChallengeRepoClonedStatusInChallengeDTO(httpSession, challengeDTO);
            model.addAttribute("challengeDTO", challengeDTO);
        }

        if(result.hasErrors()) {
            return "ctfcreator/challenge/challenge";
        }

        try {
            challengeCreationService.createNewChallenge(challengeDTO);
        } catch (Exception e) {
            return "ctfcreator/challenge/challenge";
        }

        return "redirect:/challenge-list";
    }

    private void updateChallengeRepoClonedStatusInChallengeDTO(HttpSession httpSession, ChallengeDTO challengeDTO) {
        ChallengeDTO sessionChallengeDTO = (ChallengeDTO) httpSession.getAttribute(CHALLENGE);
        if(sessionChallengeDTO != null) {
            boolean isRepoClonedSuccessfully = sessionChallengeDTO.getIsRepoClonedSuccessfully();
            challengeDTO.setRepoClonedSuccessfully(isRepoClonedSuccessfully);
        }
    }
}
