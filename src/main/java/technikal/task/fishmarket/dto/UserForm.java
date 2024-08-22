package technikal.task.fishmarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikolay Boyko
 */
@Getter
@Setter
public class UserForm {

    @NotNull(message = "Поле username обов'язкове")
    private String username;

    @NotNull(message = "Поле password обов'язкове")
    private String password;

}
