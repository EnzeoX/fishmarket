package technikal.task.fishmarket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
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
            case "HttpClientErrorException":
                if (e instanceof HttpClientErrorException.Unauthorized) {
                    log.warn("User not authorized");
                    return new ModelAndView("error/403");
                }
            case "NoResourceFoundException":
                return new ModelAndView("error/404");
            case "FishCreationException":
                return new ModelAndView("redirect:/fish/create");
            case "BadCredentialsException":
                view = new ModelAndView("redirect:/user/login");
                redirectAttributes.addFlashAttribute("errorMessage", "Невірний логін чи пароль");
                return view;
            default:
                view = new ModelAndView("redirect:/fish");
                return view;
        }
    }
}
