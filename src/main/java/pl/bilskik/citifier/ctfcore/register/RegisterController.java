package pl.bilskik.citifier.ctfcore.register;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @GetMapping(path = "/register")
    public String getRegisterPage() {
        return "ctfcore/register/register";
    }

    @HxRequest
    @PostMapping(path = "/ctf-core/register")
    public String submitRegister(@ModelAttribute @Valid RegisterDTO register, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }

        return "redirect:/"; //TO CHANGE
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/redirect-to-login")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
