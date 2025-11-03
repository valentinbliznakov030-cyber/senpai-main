package bg.senpai_main.dtos.commentDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentAddOrRemoveRequestDto {
    private String animeName;
    private String content;
    private LocalDateTime createdOn;
}
