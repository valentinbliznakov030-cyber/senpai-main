package bg.senpai_main.dtos.commentDtos;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import lombok.*;

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
}
