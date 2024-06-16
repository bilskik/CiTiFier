package pl.bilskik.citifier.ctfcreator.challenge;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/ctf-creator/challenge")
public class HtmxChallengeController {


    @HxRequest
    @PostMapping("/challenge-name")
    @ResponseBody
    public String validateChallengeName(@RequestParam @Nullable String challengeName) {
        if(challengeName == null || challengeName.isEmpty()) {
            return "Stary to nie może być puste!";
        }
        return "";
    }

    @HxRequest
    @PostMapping("/challenge-description")
    @ResponseBody
    public String validateChallengeDescription(@RequestParam @Nullable String challengeDescription) {
        if(challengeDescription == null || challengeDescription.isEmpty()) {
            return "Stary to nie może być puste!";
        }
        return "";
    }

    @HxRequest
    @PostMapping("/challenge-finish")
    @ResponseBody
    public String validateChallengeStartAndFinish(
            @RequestParam @Nullable LocalDateTime start,
            @RequestParam @Nullable LocalDateTime finish
    ) {
        if(start == null) {
            return "Stary dodaj start";
        } else if(finish == null) {
            return "Stary dodaj finito";
        } else if(!start.isBefore(finish)) {
            return "Stary before przed afterem";
        }
        return "baza64";
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
    public void loadGithubLink(@RequestParam @Nullable String githubLink) {
        //call github and build app
    }

    @HxRequest
    @PostMapping("/min-points")
    @ResponseBody
    public String validateMinPoint(
            @RequestParam @Nullable Integer minPoints,
            @RequestParam @Nullable Integer maxPoints
    ) {
        if(minPoints == null || minPoints < 0) {
            return "Minimum musi byc wieksze od 0!";
        } else if(maxPoints != null && minPoints > maxPoints) {
            return "Maximum musi być większe od minimum!";
        }
        return "";
    }


    @HxRequest
    @PostMapping("/max-points")
    @ResponseBody
    public String validatePoints(
            @RequestParam @Nullable Integer maxPoints,
            @RequestParam @Nullable Integer minPoints
    ) {
        if(maxPoints == null || maxPoints < 0) {
            return "Maximum musi być większe od 0!";
        } else if(minPoints != null && minPoints > maxPoints) {
            return "Maximum musi być większ niż minimum!";
        }
        return "";
    }

    @HxRequest
    @PostMapping
    @ResponseBody
    public void submitChallenge(@Valid ChallengeDTO challengeDTO) {
        System.out.println(challengeDTO.toString());
    }
}
