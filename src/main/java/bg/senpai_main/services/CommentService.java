package bg.senpai_main.services;

import bg.senpai_main.dtos.adminDtos.AdminCommentUpdateDto;
import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Comment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {

    Comment addComment(UUID id, CommentAddRequestDto dto);
    void removeComment(UUID commentId);

    Comment updateComment(UUID commentEditor, CommentChangeRequestDto commentChangeRequestDto);

    Page<Comment> getCommentsForEpisode(UUID animeId, int pageNumber, int sizeNumber);

    Optional<Comment> getById(UUID commentId);

    Page<Comment> findAll(int page, int size);
    List<Comment> findFilteredComments(
            UUID commentId,
            String content,
            String username,
            String animeId,
            String animeTitle,
            LocalDateTime createdOn,
            LocalDateTime updateOn
    );

    Comment updateCommentByAdmin(AdminCommentUpdateDto adminCommentUpdateDto);
}
