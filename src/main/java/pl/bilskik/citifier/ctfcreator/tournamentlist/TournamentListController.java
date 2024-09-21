package pl.bilskik.citifier.ctfcreator.tournamentlist;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRedirect;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.bilskik.citifier.ctfcreator.tournament.TournamentDTO;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TournamentListController {

    private final TournamentService tournamentService;

    @GetMapping("/tournament-list")
    public String tournamentList(Model model) {
        List<TournamentDTO> tournamentList = tournamentService.findAllTournamentsByLogin("123");

        model.addAttribute("tournamentList", tournamentList);

        return "ctfcreator/tournament/tournament-list";
    }

    @GetMapping("/ctf-creator/tournaments/new-tournament")
    @HxRequest
    @HxRedirect("/tournament")
    public String redirectToTournamentCreation() {
        return "fragments/empty";
    }
}
