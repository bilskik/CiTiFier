package pl.bilskik.citifier.web.login;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping( "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "ctfcore/login/login";
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/login/redirect-to-register")
    public String redirectToRegister() {
        return "redirect:/register";
    }
}
