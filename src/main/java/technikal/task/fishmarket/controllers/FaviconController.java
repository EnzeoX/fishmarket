package technikal.task.fishmarket.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nikolay Boyko
 */

@Controller
public class FaviconController {

    @ResponseBody
    @GetMapping("favicon.ico")
    void returnNoFavicon() {
    }
}
