package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.adminDtos.AdminCommentUpdateDto;
import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.CommentRepository;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.EpisodeService;
import bg.senpai_main.services.MemberService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bg.senpai_main.entities.Episode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final EpisodeService episodeService;
    @Override
    @Transactional
    public Comment addComment(UUID memberId, CommentAddRequestDto dto) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        Episode episode = episodeService.findById(dto.getEpisodeId())
                .orElseThrow(() -> new EntityNotFoundException("Episode not found"));

        String content = dto.getContent();

        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .createdOn(LocalDateTime.now())
                .episode(episode)
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
    public Page<Comment> getCommentsForEpisode(UUID animeId, int pageNumber, int sizeNumber){
        Episode episode = episodeService.findById(animeId).orElseThrow(() -> new EntityNotFoundException("Episode not found"));

        return commentRepository.findByEpisode(episode, PageRequest.of(pageNumber, sizeNumber));
    }


    @Override
    public Optional<Comment> getById(UUID commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Page<Comment> findAll(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Comment> findFilteredComments(
            UUID commentId,
            String content,
            String username,
            String episodeId,
            String animeTitle,
            LocalDateTime createdOn,
            LocalDateTime updateOn) {

        return commentRepository.findAll((root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();


            if (commentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), commentId));
            }

            if (content != null && !content.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
            }

            if (createdOn != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdOn"), createdOn));
            }

            if (updateOn != null) {
                predicates.add(criteriaBuilder.equal(root.get("updateOn"), updateOn));
            }


            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("member").get("username"), username));
            }

            if (episodeId != null && !episodeId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("episode").get("id"), episodeId));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Comment updateCommentByAdmin(AdminCommentUpdateDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        dto.getContent().ifPresent(comment::setContent);
        comment.setUpdatedOn(LocalDateTime.now());

        return commentRepository.save(comment);
    }
}
