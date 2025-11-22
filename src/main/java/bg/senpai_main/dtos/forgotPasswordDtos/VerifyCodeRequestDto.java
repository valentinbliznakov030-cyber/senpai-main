package bg.senpai_main.dtos.forgotPasswordDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyCodeRequestDto {
    private String email;
    private String code;
}
