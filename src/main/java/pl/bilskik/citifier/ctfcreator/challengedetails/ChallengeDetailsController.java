package pl.bilskik.citifier.ctfcreator.challengedetails;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeAppDataDTO;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;

@Controller
@RequiredArgsConstructor
public class ChallengeDetailsController {

    private final ChallengeDetailsService challengeDetailsService;

    @GetMapping("/challenge-details")
    public String getChallengeDetails(Model model) {
        Long challengeId = (Long) model.getAttribute("challengeId");
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);
        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-deploy-start")
    @HxRequest
    public String createAndStartApp(Model model, Long challengeId) {
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);

        try {
            challengeDetailsService.createAndStartApp(challengeDTO);
        } catch(Exception e) {
            challengeDetailsService.updateStatus(challengeDTO, ChallengeStatus.ERROR, challengeDTO.getChallengeId());
            model.addAttribute("applicationError", e.getMessage());
        }

        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-start")
    @HxRequest
    public String startApp(Model model, Long challengeId) {
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);

        try {
            challengeDetailsService.startApp(challengeDTO);
        } catch(Exception e) {
            challengeDetailsService.updateStatus(challengeDTO, ChallengeStatus.ERROR, challengeDTO.getChallengeId());
            model.addAttribute("applicationError", e.getMessage());
        }

        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-stop")
    @HxRequest
    public String stopApp(Model model, Long challengeId) {
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);

        try {
            challengeDetailsService.stopApp(challengeDTO);
        } catch(Exception e) {
            challengeDetailsService.updateStatus(challengeDTO, ChallengeStatus.ERROR, challengeDTO.getChallengeId());
            model.addAttribute("applicationError", e.getMessage());
        }

        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    @PostMapping("/ctf-creator/challenge-finish")
    @HxRequest
    public String finishApp(Model model, Long challengeId) {
        ChallengeDTO challengeDTO = challengeDetailsService.findChallengeById(challengeId);

        try {
            challengeDetailsService.deleteApp(challengeDTO);
        } catch(Exception e) {
            challengeDetailsService.updateStatus(challengeDTO, ChallengeStatus.ERROR, challengeDTO.getChallengeId());
            model.addAttribute("applicationError", e.getMessage());
        }

        addModelAttributes(model, challengeDTO);
        return "ctfcreator/challenge/challenge-details";
    }

    private void addModelAttributes(Model model, ChallengeDTO challengeDTO) {
        model.addAttribute("challenge", challengeDTO);
        model.addAttribute("exposedPorts", provideExposedPorts(challengeDTO.getChallengeAppDataDTO()));
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

}
