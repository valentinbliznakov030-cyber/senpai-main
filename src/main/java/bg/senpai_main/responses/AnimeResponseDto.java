package bg.senpai_main.responses;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimeResponseDto {
    private ByteArrayResource video;
}
