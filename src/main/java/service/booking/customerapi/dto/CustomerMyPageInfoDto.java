package service.booking.customerapi.dto;

public record CustomerMyPageInfoDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber
) {
}
