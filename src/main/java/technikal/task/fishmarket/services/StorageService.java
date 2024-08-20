package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.repository.FileNamesRepository;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private static final String IMAGE_DIRECTORY = "public/image";

    private final FileNamesRepository fileNamesRepository;

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

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void saveImages(MultipartFile[] files, Date saveDate) {
        for (MultipartFile file : files) {
            saveImage(file, saveDate);
        }
    }

}
