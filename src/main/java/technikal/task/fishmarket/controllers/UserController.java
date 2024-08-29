package technikal.task.fishmarket.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import technikal.task.fishmarket.dto.UserForm;
import technikal.task.fishmarket.services.UserService;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid username or password");
            return "login";
        }
        UserForm form = new UserForm();
        model.addAttribute("userForm", form);
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("userForm", form);
        log.info("Showing page registration");
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView registerUser(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
        log.info("Performing registration, provided username and password: {}, {}", userForm.getUsername(), userForm.getPassword());
        ModelAndView view;
        if (result.hasErrors()) {
            view = new ModelAndView("registration");
            view.addObject("userForm", userForm);
            view.addObject("errors", result.getAllErrors());
            return view;
        }

        userService.registerUser(userForm.getUsername(), userForm.getPassword());

        return new ModelAndView("redirect:login");
    }
}
