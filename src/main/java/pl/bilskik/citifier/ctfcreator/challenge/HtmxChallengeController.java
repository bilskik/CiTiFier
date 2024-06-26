package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/ctf-creator/challenge")
public class HtmxChallengeController {


    @HxRequest
    @PostMapping(value = "/github-link")
    @ResponseBody
    public void loadGithubLink(@Nullable String githubLink) {
        System.out.println(githubLink);
    }

    @HxRequest
    @PostMapping
    @ResponseBody
    public void submitChallenge(
            @ModelAttribute @Valid ChallengeDTO challengeDTO,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
        }
        System.out.println(challengeDTO.toString());
    }
}
