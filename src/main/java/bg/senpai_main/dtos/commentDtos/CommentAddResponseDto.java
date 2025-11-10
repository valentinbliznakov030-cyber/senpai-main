package bg.senpai_main.dtos.commentDtos;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentAddResponseDto {
    private boolean success;
    private int statusCode;
    private String content;
    private UUID commentId;
}
