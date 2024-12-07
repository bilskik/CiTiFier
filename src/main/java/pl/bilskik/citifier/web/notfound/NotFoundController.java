package pl.bilskik.citifier.web.notfound;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class NotFoundController implements ErrorController {

    @GetMapping("/error")
    public String notFound() {
        log.info("Got unknown path, redirect to not found page!");
        return "common/not-found";
    }

}
