package bg.senpai_main.web;

import bg.senpai.common.dtos.SubscriptionStatusDTO;
import bg.senpai_main.configs.MemberData;
import bg.senpai_main.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @PutMapping("/upgrade")
    public ResponseEntity<Void> upgrade(@AuthenticationPrincipal MemberData memberData) {
        service.upgrade(memberData.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SubscriptionStatusDTO> getStatus(@AuthenticationPrincipal MemberData memberData) {
        SubscriptionStatusDTO subscriptionStatusDTO = service.getStatus(memberData.getId());

        return ResponseEntity.ok(subscriptionStatusDTO);
    }
}
