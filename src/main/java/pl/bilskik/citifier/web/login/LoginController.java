package pl.bilskik.citifier.web.login;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login/login";
    }

    @HxRequest
    @GetMapping("/redirect-to-register")
    public String redirectToRegister() {
        return "redirect:/register";
    }
}
