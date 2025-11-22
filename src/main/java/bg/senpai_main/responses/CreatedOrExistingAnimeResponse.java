package bg.senpai_main.responses;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedOrExistingAnimeResponse {
    private String animeTitle;
    private UUID animeId;
}
