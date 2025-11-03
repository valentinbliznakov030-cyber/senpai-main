package bg.senpai_main.clients;

import bg.senpai_main.dtos.SubscriptionStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "subscription-service", url = "http://localhost:8081/api/subscriptions")
public interface SubscriptionClient {
    @GetMapping("/{userId}")
    SubscriptionStatusDTO getStatus(@PathVariable UUID userId);

    @PostMapping("/increment/{userId}")
    void incrementWatch(@PathVariable UUID userId);
}
