package bg.senpai_main.dtos.adminDtos;

import lombok.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommentUpdateDto {
    public UUID commentId;
    public Optional<String> content;
}
