package bg.senpai_main.dtos;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteResponseDto {
    private MemberResponseDto memberResponseDto;
    private AnimeResponseDto animeResponseDto;
}
