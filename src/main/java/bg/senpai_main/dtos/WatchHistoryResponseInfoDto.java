package bg.senpai_main.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistoryResponseInfoDto {
    private UUID watchHistoryId;
    private String animeTitle;
    private int episodeNumber;
    private LocalDateTime updatedOn;
}
