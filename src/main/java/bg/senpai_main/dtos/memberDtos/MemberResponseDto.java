package bg.senpai_main.dtos.memberDtos;

import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private String email;
    private String username;
    private Role role;
    private String profilePictureUrl;


    public static MemberResponseDto memberResponseDto(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .profilePictureUrl(member.getProfilePictureUrl())
                .role(member.getRole())
                .build();
    }
}
