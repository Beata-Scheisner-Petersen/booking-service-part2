package service.booking.reviewapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.booking.reviewapi.client.ReviewClient;
import service.booking.reviewapi.dto.NewReviewDto;
import service.booking.reviewapi.dto.ReviewResponseDto;

import java.util.List;

@RestController
public class ReviewController {

    private final ReviewClient reviewClient;

    public ReviewController(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewClient.getAllReviews(token));
    }

    @GetMapping("/reviews/user")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsFromUserId(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewClient.getReviewsFromUserId(token));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createNewReview(@RequestHeader("Authorization") String token, @RequestBody NewReviewDto newReviewDto) {
        return ResponseEntity.ok(reviewClient.createNewReview(token, newReviewDto));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewClient.getReviewById(token, reviewId));
    }

    @GetMapping("/reviews/room/avgRating/{roomNumber}")
    public ResponseEntity<Double> getAvgRating(@RequestHeader("Authorization") String token, @PathVariable("roomNumber") Integer roomNumber) {
        return ResponseEntity.ok(reviewClient.getAverageRating(token, roomNumber));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@RequestHeader("Authorization") String token, @PathVariable("reviewId") Long reviewId) {
        return reviewClient.deleteReviewById(token, reviewId);
    }

}
