package bg.senpai_main.web;

import bg.senpai_main.dtos.adminDtos.AdminCommentUpdateDto;
import bg.senpai_main.dtos.adminDtos.AdminMemberUpdateDto;
import bg.senpai_main.dtos.adminDtos.AdminMemberViewUpdateDto;
import bg.senpai_main.dtos.commentDtos.AdminCommentViewUpdateDto;
import bg.senpai_main.dtos.commentDtos.CommentGetResponseInfoDto;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.responses.CommentsFindAllResponseDto;
import bg.senpai_main.responses.AdminMemberResponseDto;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/members")
    public ResponseEntity<AdminMemberResponseDto> getAllMembers(
            @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "15") int size){

        Page<Member> memberPage = memberService.findAll(page, size);
        List<MemberResponseDto> members = memberPage.getContent().stream().map(MemberResponseDto::memberResponseDto).toList();

        AdminMemberResponseDto adminMemberResponseDto = new AdminMemberResponseDto(
                memberPage,
                members
        );

        return ResponseEntity.ok(adminMemberResponseDto);
    }

    @PutMapping("/member")
    public ResponseEntity<AdminMemberViewUpdateDto> changeMember(
            @RequestBody AdminMemberUpdateDto adminMemberUpdateDto) {

        Member updated = memberService.updateMemberByAdmin(adminMemberUpdateDto);

        AdminMemberViewUpdateDto dto = AdminMemberViewUpdateDto.builder()
                .id(updated.getId())
                .username(updated.getUsername())
                .email(updated.getEmail())
                .active(updated.isActive())
                .role(updated.getRole())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/comment")
    public ResponseEntity<AdminCommentViewUpdateDto> changeComment(@RequestBody AdminCommentUpdateDto adminCommentUpdateDto){
        Comment comment = commentService.updateCommentByAdmin(adminCommentUpdateDto);

        AdminCommentViewUpdateDto adminCommentViewUpdateDto = AdminCommentViewUpdateDto
                .builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .localDateTime(comment.getUpdatedOn())
                .build();

        return ResponseEntity.ok(adminCommentViewUpdateDto);
    }

    @GetMapping("/member/filter")
    public ResponseEntity<List<MemberResponseDto>> filterMembers(
            @RequestParam(required = false, value = "username") String username,
            @RequestParam(required = false, value = "email") String email,
            @RequestParam(required = false, value = "active") Boolean active,
            @RequestParam(required = false, value = "role") Role role,
            @RequestParam(required = false, value = "registeredOn")LocalDateTime localDateTime){
        List<MemberResponseDto> members = memberService.findFilteredMembers(username, email, active, role, localDateTime).stream().map(MemberResponseDto::memberResponseDto).toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentsFindAllResponseDto> getAllComments(
            @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "15") int size){

        Page<Comment> commentPage = commentService.findAll(page, size);
        List<CommentGetResponseInfoDto> commentGetResponseInfoDtoList = commentPage.getContent().stream().map(comment -> {
            MemberResponseDto commentCreator = MemberResponseDto.memberResponseDto(comment.getMember());
            String content = comment.getContent();

            return CommentGetResponseInfoDto
                    .builder()
                    .content(content)
                    .commentCreator(commentCreator)
                    .id(comment.getId())
                    .updatedOn(comment.getUpdatedOn())
                    .createdOn(comment.getCreatedOn())
                    .episodeId(comment.getEpisode().getId())
                    .animeTitle(comment.getEpisode().getAnime().getTitle())
                    .build();
        }).toList();

        CommentsFindAllResponseDto commentsFindAllResponseDto = new CommentsFindAllResponseDto(
                commentPage,
                commentGetResponseInfoDtoList
        );

        return ResponseEntity.ok(commentsFindAllResponseDto);
    }

    @GetMapping("/comment/filter")
    public ResponseEntity<List<CommentGetResponseInfoDto>> filterComments(
            @RequestParam(required = false, value = "commentId") UUID commentId,
            @RequestParam(required = false, value = "content") String content,
            @RequestParam(required = false, value = "createdOn") LocalDateTime createdOn,
            @RequestParam(required = false, value = "updatedOn") LocalDateTime updatedOn,

            @RequestParam(required = false, value = "username") String username,

            @RequestParam(required = false, value = "episodeId") String episodeId,
            @RequestParam(required = false, value = "animeTitle") String animeTitle

    ) {

        List<CommentGetResponseInfoDto> commentGetResponseInfoDtoList =
                commentService.findFilteredComments(commentId, content, username, episodeId, animeTitle, createdOn, updatedOn).stream().map(comment -> {

            MemberResponseDto commentCreator = MemberResponseDto.memberResponseDto(comment.getMember());

            return CommentGetResponseInfoDto
                    .builder()
                    .content(content)
                    .commentCreator(commentCreator)
                    .id(comment.getId())
                    .episodeId(comment.getEpisode().getId())
                    .animeTitle(comment.getEpisode().getAnime().getTitle())
                    .createdOn(createdOn)
                    .updatedOn(updatedOn)
                    .build();
        }).toList();

        return ResponseEntity.ok(commentGetResponseInfoDtoList);
    }

    @DeleteMapping("/profilePicture/{memberId}/{imageName}")
    public ResponseEntity<Void> deleteAnyProfilePicture(
            @PathVariable UUID memberId,
            @PathVariable String imageName) {

        memberService.adminDeleteProfilePicture(memberId, imageName);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profilePicture/{memberId}")
    public ResponseEntity<String> uploadProfilePictureForMember(
            @PathVariable UUID memberId,
            @RequestParam("file") MultipartFile file) {

        return memberService.adminUploadImage(memberId, file);
    }
}
