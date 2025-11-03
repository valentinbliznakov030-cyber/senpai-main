package bg.senpai_main.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionStatusDTO {
    private String planType;
    private int watchCount;
    private int watchLimit;
    private boolean limitReached;
}
