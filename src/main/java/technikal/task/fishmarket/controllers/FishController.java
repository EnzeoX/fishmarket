package technikal.task.fishmarket.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import technikal.task.fishmarket.dto.FishDto;
import technikal.task.fishmarket.entity.Fish;
import technikal.task.fishmarket.services.FishService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/fish")
@RequiredArgsConstructor
public class FishController {

    private final FishService fishService;

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        List<Fish> fishList = fishService.findAllFish();
        model.addAttribute("fishlist", fishList);
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
//        if (fishDto.getImageFiles() == null || fishDto.getImageFiles().length == 0) {
//            result.addError(new FieldError("fishDto", "imageFiles", "Потрібне фото рибки"));
//        }

//        if (fishDto.getImageFiles() != null && fishDto.getImageFiles().length > 2) {
//            result.addError(new FieldError("fishDto", "imageFiles", "Обмеження 3 фото на завантаження"));
//        }

        if (result.hasErrors()) {
            return "createFish";
        }
        fishService.addFish(fishDto);
        return "redirect:/fish";
    }

}
