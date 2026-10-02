package service.booking.customerapi.dto;

public record CreateCustomerDto(
        String firstname,
        String lastname,
        String identificationNumber,
        String email,
        String password,
        String phoneNumber
) {
}
