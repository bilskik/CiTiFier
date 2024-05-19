package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ctf-creator/challenge")
public class HtmxChallengeController {

    @HxRequest
    @GetMapping("/load-button")
    public String loadButton() {
        return "ctfcreator/loaded-button";
    }

    @HxRequest
    @GetMapping("/github-link/question")
    @ResponseBody
    public String loadGithubLinkQuestion() {
        return "Wrzuc tu link ze swoim repo i ciesz sie nowym challengem!";
    }

    @HxRequest
    @PostMapping("/github-link")
    @ResponseBody
    public void loadGithubLink(@RequestParam String githubLink) {
        //call github and build app
    }

    @HxRequest
    @PostMapping
    @ResponseBody
    public void submitChallenge(ChallengeDTO challengeDTO) {
    }
}
