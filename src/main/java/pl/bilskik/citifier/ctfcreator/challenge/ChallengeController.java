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

import static pl.bilskik.citifier.ctfcreator.challenge.ChallengeConstraints.CHALLENGE;


@Controller
@RequiredArgsConstructor
public class ChallengeController {

    @GetMapping(path = "/challenge")
    public String challengePage(Model model, HttpSession httpSession) {
        ChallengeInputDTO challengeInputDTO = new ChallengeInputDTO();
        if(httpSession.getAttribute(CHALLENGE) != null) {
            challengeInputDTO = (ChallengeInputDTO) httpSession.getAttribute(CHALLENGE);
        }
        model.addAttribute("challengeInputDTO", challengeInputDTO);
        return "ctfcreator/challenge/challenge";
    }

    @HxRequest
    @PostMapping(value = "/ctf-creator/challenge")
    public String submitChallenge(
            @ModelAttribute @Valid ChallengeInputDTO challengeInputDTO,
            BindingResult result,
            Model model,
            HttpSession httpSession
    ) {
        if(challengeInputDTO != null) {
            model.addAttribute("challengeInputDTO", challengeInputDTO);
        }

        if(result.hasErrors()) {
            return "ctfcreator/challenge/challenge";
        }

        httpSession.setAttribute(CHALLENGE, challengeInputDTO);
        return "redirect:/challenge-repo-link";
    }
}
