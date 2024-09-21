package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChallengeListController {

    private final ChallengeService challengeService;

    @GetMapping("/challenge-list")
    public String challengeList(Model model) {
        List<ChallengeDTO> challengeList = challengeService.findAllChallenges();
        model.addAttribute("challengeList", challengeList);

        return "ctfcreator/challenge/challenge-list";
    }

}
