package bg.senpai_main.services;

import bg.senpai.common.dtos.SubtitlesDownloadRequestDto;
import bg.senpai.common.dtos.TranslateSubtitleResponseDto;

import java.util.UUID;

public interface SubtitlesService {
    TranslateSubtitleResponseDto translateSubtitles(UUID memberId, SubtitlesDownloadRequestDto subtitlesDownloadRequestDto );
}
