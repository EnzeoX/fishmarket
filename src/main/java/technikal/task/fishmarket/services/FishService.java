package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import technikal.task.fishmarket.entity.DataFile;
import technikal.task.fishmarket.entity.Fish;
import technikal.task.fishmarket.exception.exceptions.FishCreationException;
import technikal.task.fishmarket.models.FishDto;
import technikal.task.fishmarket.repository.DataFilesRepository;
import technikal.task.fishmarket.repository.FishRepository;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final DataFilesRepository dataFilesRepository;
    private final StorageService storageService;

    private final Sort defaultValue = Sort.by(Sort.Direction.DESC, "id");

    public List<Fish> findAllFish() {
        return fishRepository.findAll(defaultValue);
    }

    public void addFish(FishDto fishDto, BindingResult result) {
        if (fishDto == null) throw new NullPointerException("Provided fishDto is null");

        if (fishDto.getImageFiles() == null || fishDto.getImageFiles().length == 0) {
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

        List<DataFile> dataFiles = Arrays.stream(fishDto.getImageFiles())
                .map(data -> {
                    DataFile dataFile = new DataFile();
                    dataFile.setFish(fish);
                    dataFile.setFileType(data.getContentType());
                    dataFile.setSaveDate(date);
                    dataFile.setFileName(date.getTime() + "_" + data.getOriginalFilename());
                    fish.getAttachedFiles().add(dataFile);
                    return dataFile;
                })
                .toList();
        fishRepository.save(fish);
        dataFilesRepository.saveAll(dataFiles);
        storageService.saveImages(fishDto.getImageFiles(), date);
    }

    public void deleteFish(int id) {
        Fish fish = fishRepository.findById(id).orElseThrow(() -> new NullPointerException("Fish by provided id not found"));
        List<String> fileNames = fish.getAttachedFiles().stream()
                .map(DataFile::getFileName)
                .toList();
        storageService.deleteImages(fileNames);
        fishRepository.delete(fish);
    }
}
