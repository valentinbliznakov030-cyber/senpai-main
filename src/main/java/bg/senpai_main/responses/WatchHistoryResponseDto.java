package bg.senpai_main.responses;

import bg.senpai_main.dtos.WatchHistoryResponseInfoDto;
import bg.senpai_main.entities.WatchHistory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class WatchHistoryResponseDto extends GlobalContentResponseDto<WatchHistory> {
    private List<WatchHistoryResponseInfoDto> watchHistoryResponseInfoDtoList;

    public WatchHistoryResponseDto(Page<WatchHistory> page, List<WatchHistoryResponseInfoDto> watchHistoryResponseInfoDtoList) {
        super(page);
        this.watchHistoryResponseInfoDtoList = watchHistoryResponseInfoDtoList;
    }
}
