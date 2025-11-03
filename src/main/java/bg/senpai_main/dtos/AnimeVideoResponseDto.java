package bg.senpai_main.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@NoArgsConstructor
public class AnimeVideoResponseDto {
    private boolean success;
    private String statusCode;
    private String message;
    String animeVideoUrl;
}
