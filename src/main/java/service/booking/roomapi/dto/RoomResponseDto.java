package service.booking.roomapi.dto;

import java.math.BigDecimal;

public record RoomResponseDto(
        Long id,
        int roomNumber,
        String roomType,
        BigDecimal roomPrice,
        int maxGuests,
        boolean extraBedAvailable) {
}
