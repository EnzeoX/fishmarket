package technikal.task.fishmarket.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;


@Getter
@Setter
public class FishDto {
	

	@NotEmpty(message = "потрібна назва рибки")
	private String name;
	@Min(0)
	private double price;
	private MultipartFile[] imageFile;

}
