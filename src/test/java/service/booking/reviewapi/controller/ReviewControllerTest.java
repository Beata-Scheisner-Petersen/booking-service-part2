package service.booking.reviewapi.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.nio.charset.*;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Test
    void createNewReview_ShouldSaveToDatabase_WhenAuthenticated() throws Exception {
        // 1. Generate a valid JWT token locally (bypassing the offline login service)
        byte[] secretKeyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);

        String token = Jwts.builder()
                .subject("1")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes))
                .compact();

        String reviewPayload = """
        {
            "roomNumber": 102,
            "reviewContent": "Integration test review content",
            "reviewScore": 5
        }
        """;

        // 2. Pass the token directly in the Authorization header to test the review endpoint
        mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewPayload))
                .andExpect(status().isOk());
    }

    @Test
    void createNewReview_ShouldReturnForbidden_WhenUnauthenticated() throws Exception {

        String jsonPayload = """
                {
                    "roomNumber": 102,
                    "reviewContent": "Integration test review content",
                    "reviewScore": 5
                }
                """;

        // Without .with(user("1")), the request is unauthenticated
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isForbidden());
    }
}