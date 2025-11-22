package bg.senpai_main.dtos.adminDtos;

import bg.senpai_main.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMemberViewUpdateDto {

    private UUID id;
    private String username;
    private String email;
    private Boolean active;
    private Role role;
}

