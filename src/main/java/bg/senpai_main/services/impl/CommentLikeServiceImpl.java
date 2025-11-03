package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.commentLikeDtos.CommentLikeOrDeleteRequest;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.CommentLike;
import bg.senpai_main.entities.Member;
import bg.senpai_main.repositories.CommentLikeRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentLikeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentService commentService;
    private final MemberService memberService;
    private final AnimeService animeService;
    private final CommentLikeRepository commentLikeRepository;


    @Override
    public void addLike(UUID likerId, CommentLikeOrDeleteRequest commentLikeRequest) {
        Member memberCreatorComment = memberService.findByUsername(commentLikeRequest.getCommentCreator()).orElseThrow(() -> new IllegalArgumentException("Member created the comment not found"));
        Member memberLikedTheComment = memberService.findById(likerId).orElseThrow(() -> new IllegalArgumentException("Member liking the comment not found"));
        Anime anime = animeService.findByTitle(commentLikeRequest.getAnimeName()).orElseThrow(() -> new IllegalArgumentException("Anime not found"));
        String content = commentLikeRequest.getContent();
        LocalDateTime createdOnComment = commentLikeRequest.getCreatedOnComment();
        Comment comment = commentService.findByContentAndAnimeAndMemberAndCreatedOn(anime, memberCreatorComment, content, createdOnComment).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(memberLikedTheComment)
                .build();

        commentLikeRepository.save(commentLike);
    }

    @Override
    public void deleteLike(UUID likerId, CommentLikeOrDeleteRequest commentDeleteRequest){
        Member memberCreatorComment = memberService.findByUsername(commentDeleteRequest.getCommentCreator()).orElseThrow(() -> new IllegalArgumentException("Member created the comment not found"));
        LocalDateTime createdOnComment = commentDeleteRequest.getCreatedOnComment();
        Member memberLikedTheComment = memberService.findById(likerId).orElseThrow(() -> new IllegalArgumentException("Member liking the comment not found"));
        Anime anime = animeService.findByTitle(commentDeleteRequest.getAnimeName()).orElseThrow(() -> new IllegalArgumentException("Anime not found"));
        String content = commentDeleteRequest.getContent();

        Comment comment = commentService.findByContentAndAnimeAndMemberAndCreatedOn(anime, memberCreatorComment, content, createdOnComment).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        CommentLike commentLike = findByMemberAndComment(memberLikedTheComment, comment).orElseThrow(() -> new IllegalArgumentException("Comment like not found"));

        commentLikeRepository.delete(commentLike);

    }

    @Override
    public Optional<CommentLike> findByMemberAndComment(Member member, Comment comment){
        return commentLikeRepository.findByMemberAndComment(member, comment);
    }




}
