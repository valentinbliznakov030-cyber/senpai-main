package bg.senpai_main.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeCreationResponseDto {
    private Integer episodeNumber;
    private String m3u8Link;
    private String sessionId;
    private UUID episodeId;
}
