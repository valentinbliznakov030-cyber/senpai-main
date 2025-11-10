package bg.senpai_main.services;

import bg.senpai.common.dtos.SubtitlesDownloadRequestDto;
import bg.senpai.common.dtos.SubtitlesDownloadedResponseDto;

import java.util.UUID;

public interface SubtitlesService {
    SubtitlesDownloadedResponseDto downloadSubtitles(UUID memberId, SubtitlesDownloadRequestDto subtitlesDownloadRequestDto );
}
