package bg.senpai_main.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnimeInfoRequestDto {
    private String animeTitle;
    private int episodeNumber;
    private String animeUrl;
}
