package technikal.task.fishmarket.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import technikal.task.fishmarket.entity.Fish;
import technikal.task.fishmarket.dto.FishDto;
import technikal.task.fishmarket.services.FishService;

import java.util.List;

@Controller
@RequestMapping("/fish")
@RequiredArgsConstructor
public class FishController {

    private final FishService fishService;

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        List<Fish> fishlist = fishService.findAllFish();
        model.addAttribute("fishlist", fishlist);
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        FishDto fishDto = new FishDto();
        model.addAttribute("fishDto", fishDto);
        return "createFish";
    }

    @GetMapping("/delete")
    public String deleteFish(@RequestParam int id) {
        fishService.deleteFish(id);
        return "redirect:/fish";
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {
        fishService.addFish(fishDto, result);
        return "redirect:/fish";
    }

}
