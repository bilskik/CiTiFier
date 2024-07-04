package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChallengeController {

    private final static List<String> challengeTypes;
    private final static List<String> flagGenerationMethod;

    static {
        challengeTypes = ChallengeType.convertToList();
        flagGenerationMethod = FlagGenerationMethod.convertToList();
    }

    private final ChallengeCreationService challengeCreationService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("challengeTypes", challengeTypes);
        model.addAttribute("flagGenerationMethods", flagGenerationMethod);
    }

    @GetMapping(path = "/challenge")
    public String challengePage(Model model) {
        return "ctfcreator/challenge/challenge";
    }

    @HxRequest
    @PostMapping(value = "/ctf-creator/challenge/github-link")
    @ResponseBody
    public void loadGithubLink(@Nullable String githubLink) {
        System.out.println(githubLink);
    }

    @HxRequest
    @PostMapping
    @ResponseBody
    public String submitChallenge(
            @ModelAttribute @Valid ChallengeDTO challengeDTO,
            BindingResult result,
            Model model
    ) {
        if(challengeDTO != null) {
            model.addAttribute("challengeDTO", challengeDTO);
        }

        if(result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
        }

        try {
            challengeCreationService.createNewChallenge(challengeDTO);
        } catch (Exception e) {
            return "ctfcreator/challenge/challenge";
        }

        return "ctfcreator/challenge/challenge";
    }
}
