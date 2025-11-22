package bg.senpai_main.responses;

import bg.senpai_main.dtos.FavoriteAnimeResponseInfoDto;
import bg.senpai_main.entities.Favorite;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class FavoriteAnimeResponseDto extends GlobalContentResponseDto<Favorite> {
    private List<FavoriteAnimeResponseInfoDto> animes;

    public FavoriteAnimeResponseDto(Page<Favorite> page, List<FavoriteAnimeResponseInfoDto> animes) {
        super(page);
        this.animes = animes;
    }
}
