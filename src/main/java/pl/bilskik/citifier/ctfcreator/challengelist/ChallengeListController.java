package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.docker.DockerComposeParserManager;
import pl.bilskik.citifier.ctfcreator.docker.model.DockerCompose;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.K8sResourceManager;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChallengeListController {

    private static final Logger log = LoggerFactory.getLogger(ChallengeListController.class);
    private final ChallengeListService challengeListService;


    @GetMapping("/challenge-list")
    public String challengeList(Model model, Authentication auth) {
        String login = retrieveLoginFromAuthentication(auth);
        List<ChallengeDTO> challengeList = challengeListService.findAllChallenges(login);
        model.addAttribute("challengeList", challengeList);
        return "ctfcreator/challenge/challenge-list";
    }

    @PostMapping("/ctf-creator/challenge-start")
    @ResponseBody
    public void parseComposeAndDeployApp(Long challengeId) {
        challengeListService.parseComposeAndDeployApp(challengeId);
    }

    private String retrieveLoginFromAuthentication(Authentication auth) {
        if(auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return "";
    }

}
