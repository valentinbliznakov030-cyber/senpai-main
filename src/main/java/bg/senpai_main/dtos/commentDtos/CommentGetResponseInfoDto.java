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
public class CommentGetResponseInfoDto {
    private UUID id;
    private MemberResponseDto commentCreator;
    private String content;
    private UUID episodeId;
    private String animeTitle;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
