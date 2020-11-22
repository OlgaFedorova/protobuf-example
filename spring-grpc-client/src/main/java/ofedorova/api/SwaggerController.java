package ofedorova.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping()
public class SwaggerController {

    @GetMapping(path = {"/", "/swagger"})
    public ModelAndView redirectToSwaggerUi() {
        return new ModelAndView(
                new RedirectView("/swagger-ui.html", true)
        );
    }
}
