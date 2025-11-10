package bg.senpai_main.services;

import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {

    Comment addComment(UUID id, CommentAddRequestDto dto);
    void removeComment(UUID commentId);

    Comment updateComment(UUID commentEditor, CommentChangeRequestDto commentChangeRequestDto);

    Page<Comment> getCommentsForAnime(UUID animeId, int pageNumber, int sizeNumber);

    Optional<Comment> getById(UUID commentId);
}
