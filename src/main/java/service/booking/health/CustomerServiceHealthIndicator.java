package service.booking.health;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import service.booking.customerapi.client.CustomerClient;

@Component
public class CustomerServiceHealthIndicator implements HealthIndicator {

    private final CustomerClient customerClient;

    public CustomerServiceHealthIndicator(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @Override
    public @Nullable Health health() {
        try {
            customerClient.getHealth();
            return Health.up()
                    .withDetail("customer-service", "Available")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("customer-service", "Unavailable")
                    .withException(e)
                    .build();
        }
    }
}
