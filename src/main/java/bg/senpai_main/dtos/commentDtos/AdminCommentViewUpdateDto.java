package bg.senpai_main.dtos.commentDtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommentViewUpdateDto {
    public UUID commentId;
    public String content;
    public LocalDateTime localDateTime;
}
