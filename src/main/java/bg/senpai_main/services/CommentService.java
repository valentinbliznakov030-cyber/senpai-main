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

    CommentResponseInfoDto addComment(UUID id, CommentAddOrRemoveRequestDto dto);
    void removeComment(UUID id, CommentAddOrRemoveRequestDto commentRemoveRequestDto);

    Optional<Comment> findByContentAndAnimeAndMemberAndCreatedOn(Anime anime, Member member, String content, LocalDateTime createdOn);

    Comment updateComment(UUID id, CommentChangeRequestDto commentChangeRequest);

    Page<Comment> getCommentsForAnime(String animeName, int pageNumber, int sizeNumber);

    Optional<Comment> getById(UUID commentId);
}
