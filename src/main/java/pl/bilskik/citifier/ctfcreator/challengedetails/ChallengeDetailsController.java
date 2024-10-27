package pl.bilskik.citifier.ctfcreator.challengedetails;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;

@Controller
@RequiredArgsConstructor
public class ChallengeDetailsController {

    private final ChallengeDetailsService challengeDetailsService;

    @GetMapping("/challenge-details")
    public String getChallengeDetails(Model model) {
        Long challengeId = (Long) model.getAttribute("challengeId");
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);
        model.addAttribute("challenge", challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-deploy-start")
    @HxRequest
    public void deployAndStartApp(Long challengeId) {
        challengeDetailsService.startAndDeployApp(challengeId);
    }

    @PostMapping("/ctf-creator/challenge-start")
    @HxRequest
    public void startApp(Long challengeId) {
        challengeDetailsService.startApp(challengeId);
    }

    @PostMapping("/ctf-creator/challenge-stop")
    @HxRequest
    public void stopApp(Long challengeId) {
        challengeDetailsService.stopApp(challengeId);
    }

    @PostMapping("/ctf-creator/challenge-finish")
    @HxRequest
    public void finishApp(Long challengeId) {
        challengeDetailsService.deleteApp(challengeId);
    }

}
