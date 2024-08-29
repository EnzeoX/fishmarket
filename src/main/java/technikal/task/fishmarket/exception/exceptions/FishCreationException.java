package technikal.task.fishmarket.exception.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * @author Nikolay Boyko
 */

@Getter
public class FishCreationException extends NullPointerException {

    private BindingResult result;

    public FishCreationException() {
    }

    public FishCreationException(String message) {
        super(message);
    }

    public FishCreationException(BindingResult result) {
        this.result = result;
    }
}
