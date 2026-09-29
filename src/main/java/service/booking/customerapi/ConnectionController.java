package service.booking.customerapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.http.StreamingHttpOutputMessage.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import service.booking.dto.CreateCustomerRequest;
import service.booking.dto.CustomerInfo;
import service.booking.dto.LoginDto;
import service.booking.dto.UpdateDto;

@RestController
@RequestMapping("/connect")
public class ConnectionController {
    private final RestClient restClient;

    public ConnectionController(@Value("${CUSTOMER_DB_CLIENT_URL:http://customer-service:8081}") String baseUrl) {
        this.restClient = RestClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCustomerRequest request) {
        try {
            return (restClient
                    .post()
                    .uri("/api/customers/create")
                    .body(request)
                    .retrieve()
                    .toEntity(Object.class)
            );
        } catch (HttpClientErrorException e) {
            return (ResponseEntity
                    .status(409)
                    .body(e.getResponseBodyAsString())
            );
        } catch (org.springframework.web.client.ResourceAccessException e) {
            return (ResponseEntity
                            .status(404)
                            .body(e.getCause())
            );
        } catch (service.booking.exceptionhandler.customexeptions.ExternalServiceConnectionException e) {
            return (ResponseEntity
                    .status(500)
                    .body(e.getCause())
            );
        } catch (Exception e) {
            return (ResponseEntity
                    .status(503)
                    .body(e.getCause())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        try {
            return (restClient
                    .post()
                    .uri("/auth/login")
                    .body(dto)
                    .retrieve()
                    .toEntity(String.class)
            );
        } catch (HttpClientErrorException e) {
            return (ResponseEntity
                    .status(400)
                    .body(e.getResponseBodyAsString())
            );
        } catch (Exception e) {
            return (ResponseEntity
                    .status(503)
                    .body(e.getCause())
            );
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> myPageData(@RequestHeader("Authorization") String jwt) {
        try {
            RestClient.RequestHeadersSpec<?> request = (RestClient.RequestHeadersSpec<?>) restClient
                    .get()
                    .uri("/api/customers/info")
                    .header("Authorization", jwt);
            return (request
                    .retrieve()
                    .toEntity(CustomerInfo.class)
            );
        } catch (HttpClientErrorException e) {
            return (ResponseEntity
                    .status(400)
                    .body(e.getResponseBodyAsString())
            );
        } catch (Exception e) {
            return (ResponseEntity
                    .status(503)
                    .body(e.getCause())
            );
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomerInfo(@RequestHeader("Authorization") String jwt,
                                                @RequestBody UpdateDto update) {
        try {
            return (restClient
                    .post()
                    .uri("/api/customers/update")
                    .header("Authorization", jwt)
                    .body(update)
                    .retrieve()
                    .toEntity(Object.class)
            );
        } catch (HttpClientErrorException e) {
            return (ResponseEntity
                    .status(400)
                    .body(e.getResponseBodyAsString())
            );
        } catch (Exception e) {
            return (ResponseEntity
                    .status(503)
                    .body(e.getCause())
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String jwt) {
        try {
            RestClient.RequestHeadersSpec<?> request = (RestClient.RequestHeadersSpec<?>) restClient
                    .delete()
                    .uri("/api/customers/delete")
                    .header("Authorization", jwt);
            return (request
                    .retrieve()
                    .toEntity(Object.class)
            );
        } catch (HttpClientErrorException e) {
            return (ResponseEntity
                    .status(400)
                    .body(e.getResponseBodyAsString())
            );
        } catch (Exception e) {
            return (ResponseEntity
                    .status(503)
                    .body(e.getCause())
            );
        }
    }
}
