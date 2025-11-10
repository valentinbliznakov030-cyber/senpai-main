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
        url = "http://localhost:8081/api/v1/anime",
        configuration = FeignConfig.class
)
public interface AnimeClient {

    @GetMapping("/m3u8Link")
    AnimeM3U8LinkDto getM3u8Link(@RequestParam("url") String animeUrl);

    @GetMapping(value = "/stream")
    ResponseEntity<Resource> streamAnime(@RequestParam("vidName") String vidName);

    @PostMapping(value="/video", produces = "application/json")
    VideoCreationResponseDto createVideo(@RequestBody VideoCreationRequestDto videoCreationRequestDto);

    @PostMapping(value = "/subtitles", produces = "application/json")
    SubtitlesDownloadedResponseDto downloadSubtitles(@RequestBody SubtitlesDownloadRequestDto subtitlesDownloadRequestDto);
}

