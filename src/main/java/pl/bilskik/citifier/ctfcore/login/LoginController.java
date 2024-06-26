package pl.bilskik.citifier.ctfcore.login;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping( "/login")
    public String getLoginPage(Model model) {
        return "ctfcore/login/login";
    }

    @PostMapping(path = "/ctf-core/login")
    public String submitLogin(@ModelAttribute @Valid LoginDTO loginDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "ctfcore/login/login";
        }
        return "redirect:/login";
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/redirect-to-register")
    public String redirectToRegister(Model model) {
        return "redirect:/register";
    }
}
