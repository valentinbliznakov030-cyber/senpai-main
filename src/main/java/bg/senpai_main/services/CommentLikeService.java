package bg.senpai_main.services;

import bg.senpai_main.dtos.commentLikeDtos.CommentLikeOrDeleteRequest;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.CommentLike;
import bg.senpai_main.entities.Member;

import java.util.Optional;
import java.util.UUID;

public interface CommentLikeService {
    void addLike(UUID likerId, CommentLikeOrDeleteRequest commentLikeRequest);
    void deleteLike(UUID likerId, CommentLikeOrDeleteRequest commentLikeRequest);
    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);
}
