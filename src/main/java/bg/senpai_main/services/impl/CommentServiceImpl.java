package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.CommentRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
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
    public Comment addComment(UUID memberId, CommentAddRequestDto dto) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        Anime anime = animeService.findById(dto.getAnimeId())
                .orElseThrow(() -> new EntityNotFoundException("Anime not found"));

        System.out.println(anime.getTitle());
        System.out.println(anime.getId());
        System.out.println(anime.getM3u8Link());
        String content = dto.getContent();

        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .createdOn(LocalDateTime.now())
                .anime(anime)
                .build();

        return commentRepository.save(comment);
    }


    @Override
    public void removeComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }



    @Override
    public Comment updateComment(UUID commentEditorId, CommentChangeRequestDto commentChangeRequestDto) {
        Comment foundCommentToChange = commentRepository.findById(commentChangeRequestDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("Comment already deleted"));
        Member commentAuthor = memberService.findById(foundCommentToChange.getMember().getId()).orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if(commentAuthor.getId().compareTo(commentEditorId) != 0){
            throw new IllegalArgumentException("Cannot change others comments");
        }

        foundCommentToChange.setContent(commentChangeRequestDto.getNewContent());

        return commentRepository.save(foundCommentToChange);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsForAnime(UUID animeId, int pageNumber, int sizeNumber){
        Anime anime = animeService.findById(animeId).orElseThrow(() -> new EntityNotFoundException("Anime not found"));

        return commentRepository.findByAnime(anime, PageRequest.of(pageNumber, sizeNumber));
    }


    @Override
    public Optional<Comment> getById(UUID commentId) {
        return commentRepository.findById(commentId);
    }
}
