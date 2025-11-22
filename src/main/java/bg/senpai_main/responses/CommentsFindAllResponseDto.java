package bg.senpai_main.responses;

import bg.senpai_main.dtos.commentDtos.CommentGetResponseInfoDto;
import bg.senpai_main.entities.Comment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class CommentsFindAllResponseDto extends GlobalContentResponseDto<Comment> {
    private List<CommentGetResponseInfoDto> comments;

    public CommentsFindAllResponseDto(Page<Comment> page, List<CommentGetResponseInfoDto> comments) {
        super(page);
        this.comments = comments;
    }
}
