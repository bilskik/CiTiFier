package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChallengeListController {

    private final ChallengeDao challengeDao;

    @GetMapping("/challenge-list")
    public String challengeList(Model model, Authentication auth) {
        String login = retrieveLoginFromAuthentication(auth);
        List<ChallengeDTO> challengeList = challengeDao.findAllByLogin(login);
        model.addAttribute("challengeList", challengeList);
        return "ctfcreator/challenge/challenge-list";
    }

    @PostMapping("/ctf-creator/challenge-details")
    public RedirectView parseComposeAndDeployApp(Model model, Long challengeId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("challengeId", challengeId);
        return new RedirectView("/challenge-details");
    }

    private String retrieveLoginFromAuthentication(Authentication auth) {
        if(auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return "";
    }

}
