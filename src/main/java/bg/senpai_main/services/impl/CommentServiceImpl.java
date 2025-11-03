package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import bg.senpai_main.repositories.CommentRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final AnimeService animeService;

    @Override
    @Transactional
    public CommentResponseInfoDto addComment(UUID id, CommentAddOrRemoveRequestDto dto) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Anime anime = animeService.findByTitle(dto.getAnimeName())
                .orElseThrow(() -> new IllegalArgumentException("Anime not found"));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .member(member)
                .createdOn(LocalDateTime.now())
                .anime(anime)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseInfoDto.builder()
                .content(saved.getContent())
                .likes(saved.getLikes().size())
                .isLikedByCurrentMember(false)
                .commentCreator(MemberResponseDto.memberResponseDto(saved.getMember()))
                .createdOn(saved.getCreatedOn())
                .build();
    }


    @Override
    public void removeComment(UUID id, CommentAddOrRemoveRequestDto commentRemoveRequestDto) {
        Member member = memberService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Anime anime = animeService.findByTitle(commentRemoveRequestDto.getAnimeName()).orElseThrow(() -> new IllegalArgumentException("Anime not found"));
        String content = commentRemoveRequestDto.getContent();
        LocalDateTime createdOn = commentRemoveRequestDto.getCreatedOn();

        Comment foundComment = commentRepository.findByContentAndAnimeAndMemberAndCreatedOn(content, anime, member, createdOn).orElseThrow(() -> new IllegalArgumentException("Comment already deleted"));

        commentRepository.delete(foundComment);

    }

    @Override
    public Optional<Comment> findByContentAndAnimeAndMemberAndCreatedOn(Anime anime, Member member, String content, LocalDateTime createdOn) {
        return commentRepository.findByContentAndAnimeAndMemberAndCreatedOn(content, anime, member, createdOn);
    }

    @Override
    public Comment updateComment(UUID id, CommentChangeRequestDto commentChangeRequestDto) {
        Member member = memberService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Anime anime = animeService.findByTitle(commentChangeRequestDto.getAnimeName()).orElseThrow(() -> new IllegalArgumentException("Anime not found"));
        LocalDateTime createdOn = commentChangeRequestDto.getCreatedOn();
        String oldContent = commentChangeRequestDto.getOldContent();
        String newContent = commentChangeRequestDto.getNewContent();

        Comment foundCommentToChange = commentRepository.findByContentAndAnimeAndMemberAndCreatedOn(oldContent, anime, member, createdOn).orElseThrow(() -> new IllegalArgumentException("Comment already deleted"));

        foundCommentToChange.setContent(newContent);

        return commentRepository.save(foundCommentToChange);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsForAnime(String animeName, int pageNumber, int sizeNumber){
        //проверяваме дали анимето съществува, ако не - хвърляме грешка
        animeService.findByTitle(animeName).orElseThrow(() -> new IllegalArgumentException("Anime not found"));

        return commentRepository.findByAnime_Title(animeName, PageRequest.of(pageNumber, sizeNumber));
    }


    @Override
    public Optional<Comment> getById(UUID commentId) {
        return commentRepository.findById(commentId);
    }
}
