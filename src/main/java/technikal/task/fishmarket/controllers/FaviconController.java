package technikal.task.fishmarket.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Controller
public class FaviconController {

    @ResponseBody
    @GetMapping("favicon.ico")
    void returnNoFavicon() {
        log.info("/favicon called");
    }
}
