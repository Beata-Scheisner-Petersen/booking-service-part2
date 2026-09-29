package service.booking.reviewapi.dto;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long id,
        Long userId,
        Integer roomNumber,
        String reviewContent,
        Integer reviewScore,
        LocalDateTime creationDate,
        LocalDateTime updateDate
) {
}
