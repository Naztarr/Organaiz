package com.naz.organaiz.service.serviceImplementation;

import com.naz.organaiz.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtImplementationTest {


    @InjectMocks
    private JwtImplementation jwtImplementation;


   private final String jwtSecretKey = "e29f594c8cd8abd16c4542864" +
           "cc1d59cbbbdaae4d031fa8b2106afbe7ee212075b0ed159667f159b4" +
           "2193bcd99e1787b9791fd4f25653d08b1b93d1550edc6ce";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtImplementation, "JWT_SECRET_KEY", jwtSecretKey);
    }
        /**
     * Creates a mock user object for testing purposes.
     *
     * @return A mock User object.
     */
    private User createUser(){
        User user = new User();
        user.setFirstName("Naz");
        user.setLastName("Star");
        user.setEmail("Naz@gmail.com");
        return user;
    }

    @Test
    public void testExtractEmailAddressFromToken() {
        User user = createUser();
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey)))
                .compact();

        String email = jwtImplementation.extractEmailAddressFromToken(token);
        assertNotNull(email);
        assertEquals("Naz@gmail.com", email);
    }

    @Test
    public void testGenerateJwtToken() {
        User user = createUser();
        Map<String, Object> claims = new HashMap<>();
        claims.put("first_name", user.getFirstName());
        claims.put("last_name", user.getLastName());

        Long expiryDate = 86400000L;

        String token = jwtImplementation.generateJwtToken(claims, user.getEmail(), expiryDate);
        Claims body = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertNotNull(token);
        assertEquals("Naz@gmail.com", body.getSubject());
        assertNotNull(body.getExpiration());
        assertFalse(jwtImplementation.isExpired(token));
    }

}




