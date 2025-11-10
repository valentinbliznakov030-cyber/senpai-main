package bg.senpai_main.dtos.commentDtos;

import bg.senpai_main.entities.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentPageData {
    private Page<Comment> commentsForAnime;
    private List<CommentGetResponseInfoDto> commentGetResponseInfoDtoList;
}
