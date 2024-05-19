package pl.bilskik.citifier.ctfcreator.challenge;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageChallengeController {

    @GetMapping(path = "/")
    public String index() {
        return "ctfcreator/challenge/challenge";
    }
}
