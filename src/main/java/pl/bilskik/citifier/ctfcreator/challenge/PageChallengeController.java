package pl.bilskik.citifier.ctfcreator.challenge;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageChallengeController {

    @GetMapping(path = "/")
    public String challengePage(Model model) {
        model.addAttribute("challengeTypes", ChallengeType.convertToList());
        model.addAttribute("flagGenerationMethods", FlagGenerationMethod.convertToList());

        return "ctfcreator/challenge/challenge";
    }
}
