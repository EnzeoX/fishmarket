package technikal.task.fishmarket.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.dto.UserForm;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nikolay Boyko
 */

@Component
public class JwtUtils {

    @Value("${secret-key.value}")
    private String SECRET_KEY;

    @Value("${token.access.validity}")
    public static int accessTokenValidity = 10000;

    public static final String JWT_COOKIE_NAME = "jwt";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, String> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername());
    }

    public String generateToken(Map<String, String> extraClaims, String username) {
        extraClaims.put("iss", "technical-task");
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(60L * accessTokenValidity)))
                .signWith(getSignKey())
                .compact();
    }

    public ResponseCookie generateJwtCookie(UserForm userPrincipal) {
        String jwt = generateToken(userPrincipal.getUsername());
        return generateCookie(JWT_COOKIE_NAME, jwt, "/api");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).before(extractExpiration(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).maxAge(accessTokenValidity).httpOnly(true).build();
    }

}
