package technikal.task.fishmarket.utils;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * @author Nikolay Boyko
 */
public class JwtUtilsTest {


    @Test
    public void generateSecretKey() {
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        String key = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.printf("Key: [%s]%n", key);
        Assertions.assertNotNull(key);
    }
}
