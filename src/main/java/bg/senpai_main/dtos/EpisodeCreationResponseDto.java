package bg.senpai_main.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeCreationResponseDto {
    private Integer episodeNumber;
    private String m3u8Link;
    private String sessionId;
}
