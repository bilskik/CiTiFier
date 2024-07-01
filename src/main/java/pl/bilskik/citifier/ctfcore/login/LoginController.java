package pl.bilskik.citifier.ctfcore.login;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxPushUrl;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping( "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginExceptionMessage", null);
        return "ctfcore/login/login";
    }

    @HxRequest
    @PostMapping(path = "/ctf-core/login")
    public String submitLogin(
            @ModelAttribute @Valid LoginDTO loginDTO,
            BindingResult bindingResult,
            Model model
    ) {

        if(loginDTO != null) {
            model.addAttribute("loginDTO", loginDTO);
        }

        if(bindingResult.hasErrors()) {
            return "ctfcore/login/login";
        }

        try {
            loginService.login(loginDTO);
        } catch(LoginException e) {
            model.addAttribute("loginExceptionMessage", e.getMessage());
            return "ctfcore/login/login";
        } catch(Exception e) {
            model.addAttribute("loginExceptionMessage", "Ooops cos poszlo nie tak!");
            return "ctfcore/login/login";
        }

        return "redirect:/challenge";
    }

    @HxRequest
    @PostMapping(path = "/ctf-core/login/tournament-code")
    public String submitTournamentCode(@RequestParam(name = "ctfCreator", required = false) boolean ctfCreator) {
        return ctfCreator ? "fragments/empty" : "ctfcore/login/tournament-code";
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/login/redirect-to-register")
    public String redirectToRegister(Model model) {
        return "redirect:/register";
    }
}
