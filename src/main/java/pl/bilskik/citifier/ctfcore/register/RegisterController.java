package pl.bilskik.citifier.ctfcore.register;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping(path = "/register")
    public String getRegisterPage() {
        return "ctfcore/register/register";
    }

    @HxRequest
    @PostMapping(path = "/ctf-core/register")
    public String submitRegister(
            @ModelAttribute @Valid RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model
    ) {

        if(registerDTO != null) {
            model.addAttribute("registerDTO", registerDTO);
        }

        if(bindingResult.hasErrors()) {
            return "ctfcore/register/register";
        }

        try {
            registerService.register(registerDTO);
        } catch(Exception e) {
            model.addAttribute("registerExceptionMessage", "Oops, coś poszło nie tak :c!");
            return "ctfcore/register/register";
        }

        return "redirect:/challenge"; //TO CHANGE
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/redirect-to-login")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
