package service.booking.customerapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import service.booking.customerapi.dto.CreateCustomerDto;
import service.booking.customerapi.dto.CustomerMyPageInfoDto;
import service.booking.customerapi.dto.LoginCustomerDto;
import service.booking.customerapi.dto.UpdateCustomerDto;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(@Value("${CUSTOMER_DB_CLIENT_URL:http://customer-service:8081}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ResponseEntity<Void> createCustomer(CreateCustomerDto request) {
        return restClient.post()
                .uri("/api/customers/create")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public ResponseEntity<String> login(LoginCustomerDto dto) {
        return restClient.post()
                .uri("/auth/login")
                .body(dto)
                .retrieve()
                .toEntity(String.class);
    }

    public ResponseEntity<CustomerMyPageInfoDto> getCustomerInfo(String token) {
        return restClient.get()
                .uri("/api/customers/info")
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .toEntity(CustomerMyPageInfoDto.class);
    }

    public ResponseEntity<Object> updateCustomer(String token, UpdateCustomerDto update) {
        return restClient.post()
                .uri("/api/customers/update")
                .header("Authorization", formatBearerToken(token))
                .body(update)
                .retrieve()
                .toEntity(Object.class);
    }

    public boolean customerExists(String token) {
            return Boolean.TRUE.equals(restClient.get()
                    .uri("/api/customers/does-customer-exist")
                    .header("Authorization", formatBearerToken(token))
                    .retrieve()
                    .body(Boolean.class));
    }

    public ResponseEntity<Object> deleteAccount(String token) {
        return restClient.delete()
                .uri("/api/customers/delete")
                .header("Authorization", formatBearerToken(token))
                .retrieve()
                .toEntity(Object.class);
    }

    private String formatBearerToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token;
        }
        return "Bearer " + token;
    }
}