package technikal.task.fishmarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class FishDto {

	@NotEmpty(message = "потрібна назва рибки")
	private String name;

	@Min(0)
	private double price;

	@Size(min = 1, max = 3, message = "Доступна кількість фото: від 1 до 3 фото на завантаження")
	@NotEmpty(message = "Необхідно додати хоча б одне фото")
	private MultipartFile[] imageFiles;

}
