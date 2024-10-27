package pl.bilskik.citifier.ctfcreator.challengedetails;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;

@Controller
@RequiredArgsConstructor
public class ChallengeDetailsController {

    private final static String SESSION_CHALLENGE = "sessionChallenge";
    private final ChallengeDetailsService challengeDetailsService;

    @GetMapping("/challenge-details")
    public String getChallengeDetails(Model model, HttpSession session) {
        Long challengeId = (Long) model.getAttribute("challengeId");
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);
        addModelAttributes(model, challengeDTO);
        session.setAttribute(SESSION_CHALLENGE, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    private String provideExposedPorts(ChallengeAppDataDTO appDataDTO) {
        if(appDataDTO == null) {
            return "";
        }
        if(appDataDTO.getStartExposedPort() != null && appDataDTO.getNumberOfApp() != null) {
            int lastPort = appDataDTO.getStartExposedPort() + appDataDTO.getNumberOfApp();
            return appDataDTO.getStartExposedPort() + " - " + lastPort;
        }
        return "";
    }

    @PostMapping("/ctf-creator/challenge-deploy-start")
    @HxRequest
    public String deployAndStartApp(Model model, HttpSession session, Long challengeId) {
        challengeDetailsService.startAndDeployApp(challengeId);
        ChallengeDTO challengeDTO = (ChallengeDTO) session.getAttribute(SESSION_CHALLENGE);
        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-start")
    @HxRequest
    public String startApp(Model model,HttpSession session, Long challengeId) {
        challengeDetailsService.startApp(challengeId);
        ChallengeDTO challengeDTO = (ChallengeDTO) session.getAttribute(SESSION_CHALLENGE);
        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-stop")
    @HxRequest
    public String stopApp(Model model, HttpSession session, Long challengeId) {
        challengeDetailsService.stopApp(challengeId);
        ChallengeDTO challengeDTO = (ChallengeDTO) session.getAttribute(SESSION_CHALLENGE);
        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-finish")
    @HxRequest
    public String finishApp(Model model, HttpSession session, Long challengeId) {
        challengeDetailsService.deleteApp(challengeId);
        ChallengeDTO challengeDTO = (ChallengeDTO) session.getAttribute(SESSION_CHALLENGE);
        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    private void addModelAttributes(Model model, ChallengeDTO challengeDTO) {
        model.addAttribute("challenge", challengeDTO);
        model.addAttribute("exposedPorts", provideExposedPorts(challengeDTO.getChallengeAppDataDTO()));
    }

}
