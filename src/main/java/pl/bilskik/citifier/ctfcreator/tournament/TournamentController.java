package pl.bilskik.citifier.ctfcreator.tournament;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.ctfdomain.entity.Tournament;
import pl.bilskik.citifier.ctfdomain.entity.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TournamentController {

    private final static List<String> userCreationMethod;

    static {
        userCreationMethod = UserCreationMethod.convertToList();
    }

    private final TournamentCreationService tournamentService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("userCreationMethod", userCreationMethod);
    }

    @GetMapping("/tournament")
    public String tournament(Model model) {
        return "ctfcreator/tournament/tournament";
    }

    @HxRequest
    @PostMapping(path = "/ctf-creator/tournament")
    public String submitTournament(
            @ModelAttribute @Valid TournamentDTO tournamentDTO,
            BindingResult bindingResult,
            Model model
    ) {
        if(tournamentDTO != null) {
            model.addAttribute("tournamentDTO", tournamentDTO);
        }

        if(bindingResult.hasErrors()) {
            return "ctfcreator/tournament/tournament";
        }

        try {
            tournamentService.createNewTournament(tournamentDTO);
        } catch(Exception e) {
            return "ctfcreator/tournament/tournament";
        }

        return "redirect:/challenge";
    }
}
