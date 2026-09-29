package service.booking.dto;

public record CustomerInfo(
        String firstname,
        String lastname,
        String email,
        String phoneNumber
) {
}
