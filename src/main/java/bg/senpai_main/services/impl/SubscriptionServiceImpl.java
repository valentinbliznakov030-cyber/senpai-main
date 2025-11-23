package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.SubscriptionStatusDTO;
import bg.senpai_main.clients.SubscriptionClient;
import bg.senpai_main.services.FakeStripeService;
import bg.senpai_main.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionClient client;
    private final FakeStripeService stripeService;


    @Override
    @CacheEvict(value = "subscription", key = "#memberId")
    public void upgrade(UUID memberId) {
        boolean success = stripeService.processPayment(memberId);

        if (!success) {
            throw new IllegalStateException("Payment failed (fake)");
        }
        client.upgrade(memberId);
    }

    @Override
    @Cacheable(value = "subscription", key = "#id")
    public SubscriptionStatusDTO getStatus(UUID id) {
        return client.getStatus(id);
    }
}
