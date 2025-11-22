package bg.senpai_main.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeCreationRequestDto {
    private String episodeUrl;
    private Integer episodeNumber;
    private UUID animeId;
}
