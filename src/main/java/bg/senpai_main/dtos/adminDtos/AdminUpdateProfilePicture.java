package bg.senpai_main.dtos.adminDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateProfilePicture {
    private String currentFileName;
    private String newFileName;
}
