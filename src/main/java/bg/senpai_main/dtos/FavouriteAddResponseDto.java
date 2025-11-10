package bg.senpai_main.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavouriteAddResponseDto {
    private boolean success;
    private int statusCode;
    private String animeTitle;
    private UUID id;
}
