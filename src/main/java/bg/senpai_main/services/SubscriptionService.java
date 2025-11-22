package bg.senpai_main.services;

import bg.senpai.common.dtos.SubscriptionStatusDTO;

import java.util.UUID;

public interface SubscriptionService {
    void upgrade(UUID memberId);

    SubscriptionStatusDTO getStatus(UUID id);
}
