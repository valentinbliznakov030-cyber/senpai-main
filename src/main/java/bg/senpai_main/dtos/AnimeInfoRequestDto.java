package bg.senpai_main.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AnimeInfoRequestDto {
    private UUID animeId;
    private String animeTitle;
    private int episodeNumber;
    private String animeUrl;
}
