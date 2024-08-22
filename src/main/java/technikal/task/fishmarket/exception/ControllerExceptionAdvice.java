package technikal.task.fishmarket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception e, RedirectAttributes redirectAttributes) {
        String exceptionName = e.getClass().getSimpleName();
        log.error("{}, {}", exceptionName, e.getMessage());
        // i'm using it like this because i'm on old version of Intelij Idea which doesn't support pattern matching
        ModelAndView view;
        switch (exceptionName) {
            case "FishCreationException":
                return new ModelAndView("redirect:/abc.htm");
            case "BadCredentialsException":
                view = new ModelAndView("redirect:/api/v1/user/login");
                redirectAttributes.addFlashAttribute("errorMessage", "Невірний логін чи пароль");
                return view;
            default:
                view = new ModelAndView("redirect:/fish");
                return view;
        }
    }
}
