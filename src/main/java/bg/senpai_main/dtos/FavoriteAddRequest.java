package bg.senpai_main.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteAddRequest {
    private String animeName;
    private int episodeNumber;
    private String username;
}
