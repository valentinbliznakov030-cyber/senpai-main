package bg.senpai_main.dtos.commentLikeDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentLikeOrDeleteRequest {
    private String content;
    private String commentCreator;
    private String animeName;
    private int episodeNumber;
    private LocalDateTime createdOnComment;
}
