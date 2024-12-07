package pl.bilskik.citifier.web.challengelist;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.bilskik.citifier.domain.dto.ChallengeDTO;
import pl.bilskik.citifier.domain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.domain.service.ChallengeDao;

import java.util.List;

@Controller
@RequestMapping("/challenge-list")
@RequiredArgsConstructor
public class ChallengeListController {

    private final ChallengeDao challengeDao;

    @GetMapping
    public String challengeList(Model model, Authentication auth) {
        String login = retrieveLoginFromAuthentication(auth);
        List<ChallengeDTO> challengeList = challengeDao.findAllByLogin(login);
        challengeList = filterActive(challengeList);
        model.addAttribute("challengeList", challengeList);
        return "/challenge/challenge-list";
    }

    @PostMapping("/redirect")
    public RedirectView parseComposeAndDeployApp(Long challengeId, RedirectAttributes redirectAttributes) {
        return new RedirectView("/challenge-details/" + challengeId);
    }

    private String retrieveLoginFromAuthentication(Authentication auth) {
        if(auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return "";
    }

    private List<ChallengeDTO> filterActive(List<ChallengeDTO> challengeList) {
        return challengeList.stream()
                .filter((c) ->  c.getStatus() != ChallengeStatus.REMOVED)
                .toList();
    }

}
