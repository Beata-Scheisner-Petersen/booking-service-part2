package service.booking.customerapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import service.booking.customerapi.client.CustomerClient;
import service.booking.exceptionhandler.customexeptions.HaveReservationException;
import service.booking.reservation.service.ReservationService;

@Service
public class CustomerService {

    final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerClient customerClient;
    private final ReservationService reservationService;

    public CustomerService(CustomerClient customerClient, ReservationService reservationService) {
        this.customerClient = customerClient;
        this.reservationService = reservationService;
    }

    public ResponseEntity<Object> deleteAccount(Long userId, @RequestHeader("Authorization") String jwt) {
        if(reservationService.hasActiveReservation(userId)) {
            logger.warn("Customer with id: {}, tried to delete the account while having active bookings", userId);
            throw new HaveReservationException("You can't delete your account while having active bookings");
        }
        logger.info("account with id: {} is deleted", userId);

        return customerClient.deleteAccount(jwt);
    }

}
