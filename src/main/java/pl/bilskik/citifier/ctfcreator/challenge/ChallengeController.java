package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


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
    public String challengePage(Model model) {
        model.addAttribute("challengeDTO", new ChallengeDTO());
        return "ctfcreator/challenge/challenge";
    }

    @HxRequest
    @PostMapping(value = "/ctf-creator/challenge")
    public String submitChallenge(
            @ModelAttribute @Valid ChallengeDTO challengeDTO,
            BindingResult result,
            Model model
    ) {
        if(challengeDTO != null) {
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
}
