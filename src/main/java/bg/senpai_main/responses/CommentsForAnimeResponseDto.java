package bg.senpai_main.responses;


import bg.senpai_main.dtos.commentDtos.CommentGetResponseInfoDto;
import bg.senpai_main.entities.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class CommentsForAnimeResponseDto extends GlobalContentResponseDto<Comment>{
    private List<CommentGetResponseInfoDto> comments;
    private boolean isUserLogged;

    public CommentsForAnimeResponseDto(Page<Comment> page, List<CommentGetResponseInfoDto> comments, boolean isUserLogged) {
        super(page);
        this.comments = comments;
        this.isUserLogged = isUserLogged;
    }
}
