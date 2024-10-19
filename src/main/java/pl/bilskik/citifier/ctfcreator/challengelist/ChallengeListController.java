package pl.bilskik.citifier.ctfcreator.challengelist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final ChallengeListService challengeListService;


    @GetMapping("/challenge-list")
    public String challengeList(Model model) {
        List<ChallengeDTO> challengeList = challengeListService.findAllChallenges();
        model.addAttribute("challengeList", challengeList);

        return "ctfcreator/challenge/challenge-list";
    }

    @GetMapping("/ctr-creator/cluster-start")
    @ResponseBody
    public void parseComposeAndDeployApp() {
        challengeListService.parseComposeAndDeployApp(1L);
    }

}
