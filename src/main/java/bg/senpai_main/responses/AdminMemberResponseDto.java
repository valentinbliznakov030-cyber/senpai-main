package bg.senpai_main.responses;

import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.entities.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class AdminMemberResponseDto extends GlobalContentResponseDto<Member>{
    private final List<MemberResponseDto> members;

    public AdminMemberResponseDto(Page<Member> page, List<MemberResponseDto> members) {
        super(page);
        this.members = members;
    }
}
