package technikal.task.fishmarket.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import technikal.task.fishmarket.dto.FishDto;
import technikal.task.fishmarket.entity.DataFile;
import technikal.task.fishmarket.entity.Fish;
import technikal.task.fishmarket.exception.exceptions.FishCreationException;
import technikal.task.fishmarket.repository.FishRepository;

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
    private final StorageService storageService;

    private final Sort defaultValue = Sort.by(Sort.Direction.DESC, "id");

    public List<Fish> findAllFish() {
        return fishRepository.findAll(defaultValue);
    }

    @Transactional
    public void addFish(FishDto fishDto) {
        if (fishDto == null) throw new NullPointerException("Provided fishDto is null");

        Date date = new Date();
        Fish fish = new Fish();

        fish.setCatchDate(date);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());

        List<DataFile> files = Arrays.stream(fishDto.getImageFiles())
                .map(data -> {
                    DataFile dataFile = new DataFile();
                    dataFile.setFileType(data.getContentType());
                    dataFile.setSaveDate(date);
                    dataFile.setFileName(date.getTime() + "_" + data.getOriginalFilename());
                    dataFile.setFish(fish);
                    return dataFile;
                })
                .toList();
        fish.setAttachedFiles(files);
        fishRepository.save(fish);
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
