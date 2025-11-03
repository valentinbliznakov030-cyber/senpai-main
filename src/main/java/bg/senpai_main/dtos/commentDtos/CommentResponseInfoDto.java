package bg.senpai_main.dtos.commentDtos;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseInfoDto {
    private UUID id;
    private MemberResponseDto commentCreator;
    private long likes;
    private boolean isLikedByCurrentMember;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
