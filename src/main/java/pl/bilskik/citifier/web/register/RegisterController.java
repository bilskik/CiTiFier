package pl.bilskik.citifier.web.register;

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
    private final static String REGISTRATION_FAILED = "Nie można zarejstrować użytkownika!";

    private final RegisterLoginPasswordValidator validator;
    private final RegisterService registerService;

    @GetMapping(path = "/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "ctfcore/register/register";
    }

    @HxRequest
    @PostMapping(path = "/ctf-core/register")
    public String submitRegister(
            @ModelAttribute @Valid RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("registerDTO", registerDTO);

        try {
            validator.validateLogin(registerDTO.getLogin());
        } catch(RegisterException e) {
            bindingResult.rejectValue("login", "error.login", e.getMessage());
        }

        try {
            validator.validatePassword(registerDTO.getPassword());
        } catch(RegisterException e) {
            bindingResult.rejectValue("password", "error.password", e.getMessage());
        }

        if(bindingResult.hasErrors()) {
            return "ctfcore/register/register";
        }

        try {
            registerService.register(registerDTO);
        } catch(Exception e) {
            model.addAttribute("registerExceptionMessage", REGISTRATION_FAILED);
            return "ctfcore/register/register";
        }

        return "redirect:/login";
    }

    @HxRequest
    @GetMapping(path = "/ctf-core/register/redirect-to-login")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}
