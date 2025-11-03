package bg.senpai_main.dtos.commentDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentChangeRequestDto {
    private String oldContent;
    private String newContent;
    private String animeName;
    private LocalDateTime createdOn;
}
