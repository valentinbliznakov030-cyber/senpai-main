package bg.senpai_main.dtos;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimeResponseDto {
    private String title;

    public static AnimeResponseDto animeResponseDto(Anime anime){
        return AnimeResponseDto
                .builder()
                .title(anime.getTitle())
                .build();
    }
}
