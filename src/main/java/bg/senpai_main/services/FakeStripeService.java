package bg.senpai_main.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FakeStripeService {
    public boolean processPayment(UUID memberId) {
        System.out.println("FAKE STRIPE: processing payment for member " + memberId);
        return true;
    }
}
