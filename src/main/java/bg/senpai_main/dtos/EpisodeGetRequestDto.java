package bg.senpai_main.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeGetRequestDto {
    private UUID episodeId;
    private UUID animeId;
}
