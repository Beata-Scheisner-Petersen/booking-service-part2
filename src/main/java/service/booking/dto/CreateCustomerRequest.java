package service.booking.dto;

public record CreateCustomerRequest(
        String firstname,
        String lastname,
        String identificationNumber,
        String email,
        String password,
        String phoneNumber
) {
}
