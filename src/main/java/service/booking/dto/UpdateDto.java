package service.booking.dto;

public record UpdateDto(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
