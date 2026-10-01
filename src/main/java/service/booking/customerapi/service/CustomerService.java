package service.booking.customerapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import service.booking.customerapi.client.CustomerClient;
import service.booking.exceptionhandler.customexeptions.HaveReservationException;
import service.booking.reservation.service.ReservationService;

@Service
public class CustomerService {

    private final CustomerClient customerClient;
    private final ReservationService reservationService;

    public CustomerService(CustomerClient customerClient, ReservationService reservationService) {
        this.customerClient = customerClient;
        this.reservationService = reservationService;
    }

    public ResponseEntity<Object> deleteAccount(Long userId, @RequestHeader("Authorization") String jwt) {
        if(reservationService.hasActiveReservation(userId)) {
            throw new HaveReservationException("You can't delete your account while having active bookings");
        }

        return customerClient.deleteAccount(jwt);
    }

}
