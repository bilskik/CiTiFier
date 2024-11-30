package pl.bilskik.citifier.web.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static pl.bilskik.citifier.web.challenge.ChallengeConstraints.CHALLENGE;

@Controller
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

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
            HttpSession httpSession
    ) {
        if(challengeInputDTO != null) {
            model.addAttribute("challengeInputDTO", challengeInputDTO);
        }

        if(result.hasErrors()) {
            return "challenge/challenge";
        }

        httpSession.setAttribute(CHALLENGE, challengeInputDTO);
        return "redirect:/challenge-repo-link";
    }
}
