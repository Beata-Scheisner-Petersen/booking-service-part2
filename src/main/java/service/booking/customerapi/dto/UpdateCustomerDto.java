package service.booking.customerapi.dto;

public record UpdateCustomerDto(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
