package bg.senpai_main.web;

import bg.senpai.common.dtos.SubtitlesDownloadRequestDto;
import bg.senpai.common.dtos.TranslateSubtitleResponseDto;
import bg.senpai_main.configs.MemberData;
import bg.senpai_main.services.SubtitlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subtitles")
public class SubtitlesController {
    private final SubtitlesService subtitlesService;

    @PostMapping
    public ResponseEntity<TranslateSubtitleResponseDto> translateSubtitles(@AuthenticationPrincipal MemberData memberData, @RequestBody SubtitlesDownloadRequestDto subtitlesDownloadRequestDto){
        return ResponseEntity.ok(subtitlesService.translateSubtitles(memberData.getId(), subtitlesDownloadRequestDto));
    }
}
