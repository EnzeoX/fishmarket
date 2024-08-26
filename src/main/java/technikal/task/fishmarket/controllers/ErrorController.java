package technikal.task.fishmarket.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/403")
    public String getForbidden(HttpServletRequest request) {
        log.warn("Access to error pages");
        String referer = request.getHeader("Referer");
        log.warn("Refer: {}", referer);
        if (referer == null || !referer.contains(request.getServerName())) {
            return "redirect:/";
        }
        return "error/403";
    }

    @GetMapping("/404")
    public String getNotFound() {
        return "error/404";
    }

    @GetMapping("/401")
    public String getUnauthorized() {
        return "error/404";
    }
}
