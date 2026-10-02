package service.booking.customerapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import service.booking.customerapi.client.CustomerClient;
import service.booking.customerapi.dto.CreateCustomerDto;
import service.booking.customerapi.dto.LoginCustomerDto;
import service.booking.customerapi.dto.UpdateCustomerDto;
import service.booking.customerapi.service.CustomerService;

@RestController
@RequestMapping("/connect")
public class CustomerController {

    private final CustomerClient customerClient;
    private final CustomerService customerService;

    public CustomerController(CustomerClient customerClient, CustomerService customerService) {
        this.customerClient = customerClient;
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody CreateCustomerDto request) {
        return customerClient.createCustomer(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCustomerDto dto) {
        return customerClient.login(dto);
    }

    @GetMapping("/info")
    public ResponseEntity<?> myPageData(@RequestHeader("Authorization") String jwt) {
        return customerClient.getCustomerInfo(jwt);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomerInfo(@RequestHeader("Authorization") String jwt, @RequestBody UpdateCustomerDto update) {
        return customerClient.updateCustomer(jwt, update);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Long userId, @RequestHeader("Authorization") String jwt) {
        return customerService.deleteAccount(userId, jwt);
    }
}