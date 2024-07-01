package pl.bilskik.citifier.ctfcreator.tournament;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TournamentController {

    @GetMapping("/tournament")
    public String tournament(Model model) {
        return "ctfcreator/tournament/tournament";
    }

}
