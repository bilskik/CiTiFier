package pl.bilskik.citifier.ctfcreator.challenge;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageChallengeController {

    @GetMapping(path = "/")
    public String challengePage(Model model) {
        System.out.println(ChallengeTypeEnum.convertToList());
        model.addAttribute("challengeTypes", ChallengeTypeEnum.convertToList());
        model.addAttribute("flagGenerationMethods", FlagGenerationMethodEnum.convertToList());

        return "ctfcreator/challenge/challenge";
    }
}
