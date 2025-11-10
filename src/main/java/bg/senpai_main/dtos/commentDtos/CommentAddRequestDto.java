package bg.senpai_main.dtos.commentDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CommentAddRequestDto {
    private UUID animeId;
    private String content;
}
