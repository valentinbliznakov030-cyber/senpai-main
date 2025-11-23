package bg.senpai_main.clients;

import bg.senpai.common.dtos.*;
import bg.senpai_main.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "anime-service",
        url = "${anime.service.url:http://localhost:8081/api/v1/}",
        configuration = FeignConfig.class
)
public interface AnimeClient {

    @GetMapping("/anime/m3u8Link")
    ResponseEntity<AnimeM3U8LinkDto> getM3u8Link(@RequestParam("url") String animeUrl, @RequestParam("sessionId") String sessionId);

    @GetMapping(value = "/stream")
    ResponseEntity<Resource> streamAnime(@RequestParam("vidName") String vidName);

    @PostMapping(value="/anime/video", produces = "application/json")
    ResponseEntity<VideoCreationResponseDto> createVideo(@RequestBody VideoCreationRequestDto videoCreationRequestDto);

    @PostMapping(value = "/subtitles", produces = "application/json")
    ResponseEntity<SubtitlesDownloadedResponseDto> downloadSubtitles(@RequestBody SubtitlesDownloadRequestDto subtitlesDownloadRequestDto);

    @PostMapping(value = "/subtitles/translation", produces = "application/json")
    ResponseEntity<TranslateSubtitleResponseDto> translateSubtitles(@RequestBody TranslateSubtitleRequestDto translateSubtitleRequestDto);
}

