package service.booking.health;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import service.booking.customerapi.client.CustomerClient;

@Component
public class CustomerServiceHealthIndicator implements HealthIndicator {

    private final CustomerClient customerClient;
    final Logger logger = LoggerFactory.getLogger(CustomerServiceHealthIndicator.class);

    public CustomerServiceHealthIndicator(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @Override
    public @Nullable Health health() {
        try {
            customerClient.getHealth();
            logger.info("Health: customer-service is available");
            return Health.up()
                    .withDetail("customer-service", "Available")
                    .build();
        } catch (Exception e) {
            logger.info("Health: customer-service is unavailable");
            return Health.down()
                    .withDetail("customer-service", "Unavailable")
                    .withException(e)
                    .build();
        }
    }
}
