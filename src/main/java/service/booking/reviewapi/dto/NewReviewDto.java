package service.booking.reviewapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewReviewDto(
        @NotNull
        Integer roomNumber,
        @NotNull
        @Size(max=200)
        String reviewContent,
        @NotNull
        @Min(1)
        @Max(5)
        Integer reviewScore) {
}
