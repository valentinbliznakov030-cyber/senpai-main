package bg.senpai_main.dtos.commentDtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentChangeResponseDto {
    private UUID commentId;
    private String newContent;
}
