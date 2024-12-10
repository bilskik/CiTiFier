package pl.bilskik.citifier.web.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static pl.bilskik.citifier.web.challenge.ChallengeConstraints.CHALLENGE;
import static pl.bilskik.citifier.web.common.CommonWebUtils.retrieveLoginFromAuthentication;

@Controller
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public String challengePage(Model model, HttpSession httpSession) {
        ChallengeInputDTO challengeInputDTO = new ChallengeInputDTO();
        if(httpSession.getAttribute(CHALLENGE) != null) {
            challengeInputDTO = (ChallengeInputDTO) httpSession.getAttribute(CHALLENGE);
        }
        model.addAttribute("challengeInputDTO", challengeInputDTO);
        return "challenge/challenge";
    }

    @HxRequest
    @PostMapping
    public String submitChallenge(
            @ModelAttribute @Valid ChallengeInputDTO challengeInputDTO,
            BindingResult result,
            Model model,
            HttpSession httpSession,
            Authentication authentication
    ) {
        if(challengeInputDTO != null) {
            model.addAttribute("challengeInputDTO", challengeInputDTO);
        }

        String login = retrieveLoginFromAuthentication(authentication);
        boolean isNameUnique = challengeService
                .isChallengeNameUniqueForCTFCreator(login, challengeInputDTO.getName());
        if(!isNameUnique) {
            result.rejectValue("name", "error.name", "Posiadasz już zadanie z taką nazwą!");
            return "challenge/challenge";
        }

        if(result.hasErrors()) {
            return "challenge/challenge";
        }

        httpSession.setAttribute(CHALLENGE, challengeInputDTO);
        return "redirect:/challenge-repo-link";
    }
}
