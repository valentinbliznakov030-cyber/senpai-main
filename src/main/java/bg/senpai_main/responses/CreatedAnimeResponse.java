package bg.senpai_main.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedAnimeResponse {
    private String animeTitle;
    private Integer episodeNumber;
    private String m3u8Link;
}
