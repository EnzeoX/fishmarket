package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import technikal.task.fishmarket.entity.Fish;
import technikal.task.fishmarket.exception.exceptions.FishCreationException;
import technikal.task.fishmarket.models.FishDto;
import technikal.task.fishmarket.repository.FishRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class FishService {

    private final FishRepository fishRepository;
    private final StorageService storageService;

    private final Sort defaultValue = Sort.by(Sort.Direction.DESC, "id");

    public List<Fish> findAllFish() {
        return fishRepository.findAll(defaultValue);
    }

    public void addFish(FishDto fishDto, BindingResult result) {
        if (fishDto == null) throw new NullPointerException("Provided fishDto is null");

        if (fishDto.getImageFile() == null || fishDto.getImageFile().length == 0) {
            result.addError(new FieldError("fishDto", "imageFile", "Потрібне фото рибки"));
        }

        if (result.hasErrors()) {
            throw new FishCreationException(result);
        }

        Date date = new Date();
        Fish fish = new Fish();

        fish.setCatchDate(date);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());
        fish.setAttachedFiles(new ArrayList<>());


        fishRepository.save(fish);

        storageService.saveImages(fishDto.getImageFile(), date);


    }

    public void deleteFish(int id) {
        try {

            Fish fish = fishRepository.findById(id).orElseThrow(() -> new NullPointerException("Fish by provided id not found"));

//            Path imagePath = Paths.get("public/images/" + fish.getImageFileName());
//            Files.delete(imagePath);
            fishRepository.delete(fish);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}
