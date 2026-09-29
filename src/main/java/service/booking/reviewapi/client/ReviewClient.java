package service.booking.reviewapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import service.booking.reviewapi.dto.NewReviewDto;
import service.booking.reviewapi.dto.ReviewResponseDto;

import java.util.List;

@Component
public class ReviewClient {

    private final RestClient restClient;

    public ReviewClient(@Value("${REVIEW_DB_CLIENT_URL:http://review-service:8083}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ReviewResponseDto> getAllReviews(String token) {
        return restClient.get()
                .uri("/reviews")
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ReviewResponseDto>>() {
                });
    }

    public List<ReviewResponseDto> getReviewsFromUserId(String token) {
        return restClient.get()
                .uri("/reviews/user")
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ReviewResponseDto>>() {
                });
    }

    public ReviewResponseDto createNewReview(String token, NewReviewDto newReviewDto) {
            return restClient.post()
                    .uri("/reviews")
                    .header("Authorization", formatBearerToken(token))
                    .body(newReviewDto)
                    .retrieve()
                    .body(ReviewResponseDto.class);
    }

    public ReviewResponseDto getReviewById(String token, Long reviewId) {
        return restClient.get()
                .uri("/reviews/" + reviewId)
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .body(ReviewResponseDto.class);
    }

    public Double getAverageRating(String token, Integer roomNumber) {
        return restClient.get()
                .uri("/reviews/room/avgRating/" + roomNumber)
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .body(Double.class);
    }

    public ResponseEntity<String> deleteReviewById(String token, Long reviewId) {
        return restClient.delete()
                .uri("/reviews/" + reviewId)
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .toEntity(String.class);
    }


    private String formatBearerToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token;
        }
        return "Bearer " + token;
    }


}
