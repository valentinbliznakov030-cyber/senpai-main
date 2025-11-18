package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.dtos.commentDtos.*;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.responses.CommentsForEpisodeResponseDto;
import bg.senpai_main.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable UUID commentId){
        return ResponseEntity.ok(commentService.getById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found")));
    }

    @GetMapping
    public ResponseEntity<CommentsForEpisodeResponseDto> getCommentsForEpisode(@AuthenticationPrincipal MemberData memberData,
                                                                             @RequestParam("episodeId") UUID episodeId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "15") int size){

        Page<Comment> comments = commentService.getCommentsForEpisode(episodeId, page, size);

        List<CommentGetResponseInfoDto> commentGetResponseInfoDtoList = comments.getContent().stream().map(comment -> {
            MemberResponseDto commentCreator = MemberResponseDto.memberResponseDto(comment.getMember());
            String content = comment.getContent();

            return CommentGetResponseInfoDto
                    .builder()
                    .content(content)
                    .commentCreator(commentCreator)
                    .id(comment.getId())
                    .build();
        }).toList();

        CommentsForEpisodeResponseDto commentsForEpisodeResponseDto = new CommentsForEpisodeResponseDto(
                comments,
                commentGetResponseInfoDtoList,
                memberData != null
        );

        return ResponseEntity.ok(commentsForEpisodeResponseDto);
    }


    @PostMapping
    public ResponseEntity<CommentAddResponseDto> addComment(@AuthenticationPrincipal MemberData memberData, @RequestBody CommentAddRequestDto commentDto) {
        Comment comment = commentService.addComment(memberData.getId(), commentDto);

        CommentAddResponseDto commentAddResponseDto = CommentAddResponseDto.builder()
                .success(true)
                .statusCode(201)
                .content(comment.getContent())
                .commentId(comment.getId())
                .build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(comment.getId())
                .toUri();

        return ResponseEntity.created(location).body(commentAddResponseDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeComment(@AuthenticationPrincipal MemberData memberData, @PathVariable("id") UUID id){
        commentService.removeComment(id);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping()
    public ResponseEntity<?> changeComment(@AuthenticationPrincipal MemberData memberData, @RequestBody CommentChangeRequestDto commentChangeRequestDto){
        Comment comment = commentService.updateComment(memberData.getId(), commentChangeRequestDto);

        CommentChangeResponseDto commentChangeResponseDto = CommentChangeResponseDto
                .builder()
                .newContent(comment.getContent())
                .commentId(comment.getId())
                .build();

        return ResponseEntity.ok(commentChangeRequestDto);
    }

}
