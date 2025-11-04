package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.responses.CommentsForAnimeResponseDto;
import bg.senpai_main.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable UUID commentId){
        return ResponseEntity.ok(commentService.getById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found")));
    }

    @GetMapping("/anime")
    public ResponseEntity<CommentsForAnimeResponseDto> getCommentForAnime(@AuthenticationPrincipal MemberData memberData,
                                                @RequestParam("name") String animeName,
                                                @RequestParam("episodeNumber") int episodeNumber,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "15") int size){

        Page<Comment> comments = commentService.getCommentsForAnime(animeName,episodeNumber, page, size);

        List<CommentResponseInfoDto> commentResponseInfoDtoList = comments.getContent().stream().map(comment -> {
            MemberResponseDto commentCreator = MemberResponseDto.memberResponseDto(comment.getMember());
            String content = comment.getContent();
            LocalDateTime createdOn = comment.getCreatedOn();
            LocalDateTime updatedOn = comment.getUpdatedOn();
            long likes = comment.getLikes().size();
            boolean isLikedByCurrentMember = memberData != null && comment.getLikes().stream().anyMatch(like -> like.getMember().getId().compareTo(memberData.getId()) == 0);

            return CommentResponseInfoDto
                    .builder()
                    .likes(likes)
                    .createdOn(createdOn)
                    .updatedOn(updatedOn)
                    .isLikedByCurrentMember(isLikedByCurrentMember)
                    .content(content)
                    .commentCreator(commentCreator)
                    .id(comment.getId())
                    .build();
        }).toList();

        //краен респонс с инфо за пагинацията и дто визуализаицята на коментарите от сървиса
        CommentsForAnimeResponseDto commentsForAnimeResponseDto = new CommentsForAnimeResponseDto(
                comments,
                commentResponseInfoDtoList
        );

        return ResponseEntity.ok(commentsForAnimeResponseDto);
    }


    @PostMapping
    public ResponseEntity<?> addComment(@AuthenticationPrincipal MemberData memberData, CommentAddOrRemoveRequestDto commentDto) {
        CommentResponseInfoDto response = commentService.addComment(memberData.getId(), commentDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal MemberData memberData, CommentAddOrRemoveRequestDto commentDeleteRequestDto){
        commentService.removeComment(memberData.getId(), commentDeleteRequestDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
