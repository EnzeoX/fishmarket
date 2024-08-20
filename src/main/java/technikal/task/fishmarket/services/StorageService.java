package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private static final String IMAGE_DIRECTORY = "public/image";

    //TODO find a way to save and delete files/images properly by their names
    public void saveImage(MultipartFile image, Date date) {
        String storageFileName = date.getTime() + "_" + image.getOriginalFilename();
        try {
            Path uploadPath = Paths.get(IMAGE_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(IMAGE_DIRECTORY + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void saveImages(MultipartFile[] files, Date saveDate) {
        for (MultipartFile file : files) {
            saveImage(file, saveDate);
        }
    }

    public void deleteImages(List<String> imageNames) {
        imageNames
                .forEach(name -> {
                    Path imagePath = Paths.get(IMAGE_DIRECTORY + name);
                    try {
                        Files.delete(imagePath);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });


    }

}
