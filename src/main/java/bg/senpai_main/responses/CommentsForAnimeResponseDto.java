package bg.senpai_main.responses;


import bg.senpai_main.dtos.commentDtos.CommentResponseInfoDto;
import bg.senpai_main.entities.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CommentsForAnimeResponseDto extends GlobalContentResponseDto<Comment>{
    private List<CommentResponseInfoDto> comments;

    public CommentsForAnimeResponseDto(Page<Comment> page,  List<CommentResponseInfoDto> comments) {
        super(page);
        this.comments = comments;
    }
}
