package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.SubscriptionStatusDTO;
import bg.senpai_main.clients.SubscriptionClient;
import bg.senpai_main.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionClient client;

    @Override
    public void upgrade(UUID memberId) {
        client.upgrade(memberId);
    }

    @Override
    public SubscriptionStatusDTO getStatus(UUID id) {
        return client.getStatus(id);
    }
}
