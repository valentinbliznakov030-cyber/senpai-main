package bg.senpai_main.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteAnimeResponseInfoDto {
    private UUID id;
    private String animeTitle;
    private String animeM3u8Link;
}
