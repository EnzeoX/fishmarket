package technikal.task.fishmarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import technikal.task.fishmarket.enums.Role;

/**
 * @author Nikolay Boyko
 */

@Getter
@Setter
@Builder
public class UserDto {

    private int id;
    private String username;

    // I thinks this is not needed
//    private String password;

    private Role role;
}
