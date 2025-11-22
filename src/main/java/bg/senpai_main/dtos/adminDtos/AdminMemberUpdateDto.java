package bg.senpai_main.dtos.adminDtos;

import bg.senpai_main.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMemberUpdateDto {
    private UUID id;
    private Optional<String> username = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<Boolean> active = Optional.empty();
    private Optional<Role> role = Optional.empty();
}


