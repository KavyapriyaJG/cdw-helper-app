package cdw.helper.utilities;

import cdw.helper.constants.CommonConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite that holds utility methods for Jwt
 */
public class JwtUtilityTest {
    private JwtUtility jwtUtility;

    @BeforeEach
    void setUp() {
        jwtUtility = new JwtUtility();
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        String email = "kavya@mail.com";
        String role = "ROLE_RESIDENT";

        String token = jwtUtility.generateToken(email, role);
        assertNotNull(token);

        Claims claims = Jwts.parser()
                .setSigningKey(CommonConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        assertEquals(email, claims.getSubject());
        assertEquals(role, claims.get("role"));

        Date issuedAt = claims.getIssuedAt();

        // Check expiration time is 3 hours after issued time
        Date expiration = claims.getExpiration();
        assertEquals(issuedAt.getTime() + (1000 * 60 * 60 * 3), expiration.getTime());
    }

    @Test
    void generateToken_ShouldIncludeCorrectRoleInClaims() {
        String email = "kavya@mail.com";
        String role = "ROLE_HELPER";

        String token = jwtUtility.generateToken(email, role);

        Claims claims = Jwts.parser()
                .setSigningKey(CommonConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        assertEquals("ROLE_HELPER", claims.get("role"));
    }

    @Test
    void generateToken_ShouldSetCorrectExpiration() {
        String email = "kavya@mail.com";
        String role = "ROLE_RESIDENT";
        long currentTimeMillis = System.currentTimeMillis();

        String token = jwtUtility.generateToken(email, role);
        Claims claims = Jwts.parser()
                .setSigningKey(CommonConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        Date issuedAt = claims.getIssuedAt();

        assertEquals(issuedAt.getTime() + (1000 * 60 * 60 * 3), claims.getExpiration().getTime());
    }
}
