package technikal.task.fishmarket.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Nikolay Boyko
 */
public class UserAuthErrorException extends AuthenticationException {

    public UserAuthErrorException(String msg) {
        super(msg);
    }
}
