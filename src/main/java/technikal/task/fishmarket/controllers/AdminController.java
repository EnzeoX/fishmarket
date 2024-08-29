package technikal.task.fishmarket.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import technikal.task.fishmarket.dto.UserDto;
import technikal.task.fishmarket.exception.exceptions.HighAuthorityException;
import technikal.task.fishmarket.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping({"","/"})
    public String showUsersList(@RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        Page<UserDto> dtoPage = userService.getPaginatedUsers(PageRequest
                .of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "username")));
        model.addAttribute("dtoPage", dtoPage);
        int totalPages = dtoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("totalPages", dtoPage.getTotalPages());
            model.addAttribute("dtoContent", dtoPage.getContent());
        }
        return "admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("id") int id) throws HighAuthorityException {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
