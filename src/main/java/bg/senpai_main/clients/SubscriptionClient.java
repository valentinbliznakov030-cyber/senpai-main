package bg.senpai_main.clients;

import bg.senpai.common.dtos.SubscriptionStatusDTO;
import bg.senpai.common.dtos.SubscriptionUpgradeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "subscription-service", url = "http://localhost:8082/api/v1/subscriptions")
public interface SubscriptionClient {
    @GetMapping("/{userId}")
    SubscriptionStatusDTO getStatus(@PathVariable UUID userId);

    @PostMapping("/increase/{userId}")
    void incrementWatch(@PathVariable UUID userId);

    @PutMapping("/upgrade/{userId}")
    void upgrade(UUID userId);


}
