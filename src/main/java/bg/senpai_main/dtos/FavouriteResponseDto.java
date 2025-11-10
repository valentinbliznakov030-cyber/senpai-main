package bg.senpai_main.dtos;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteResponseDto {
    private MemberResponseDto member;
    private AnimeResponseDto addedAnimeToFavourites;
}
