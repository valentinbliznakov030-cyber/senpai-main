package bg.senpai_main.web;

import bg.senpai.common.dtos.VideoCreationRequestDto;
import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.dtos.EpisodeCreationRequestDto;
import bg.senpai_main.dtos.EpisodeCreationResponseDto;
import bg.senpai_main.dtos.EpisodeGetRequestDto;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/episode")
public class EpisodeController {
    private final EpisodeService episodeService;

    @PostMapping
    public ResponseEntity<EpisodeCreationResponseDto> createOrGetEpisode(@RequestBody EpisodeCreationRequestDto episodeCreationRequestDto) {
        String sessionId = UUID.randomUUID().toString() + "_" +
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                        .format(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        Episode episode = episodeService.getEpisode(episodeCreationRequestDto, sessionId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(episode.getId())
                .toUri();

        EpisodeCreationResponseDto response = EpisodeCreationResponseDto
                .builder()
                .sessionId(sessionId)
                .m3u8Link(episode.getM3u8Link())
                .episodeNumber(episode.getEpisodeNumber())
                .episodeId(episode.getId())
                .build();

        return ResponseEntity.created(uri).body(response);
    }



    @PostMapping("/video")
    public ResponseEntity<VideoCreationResponseDto> createVideo(@RequestBody VideoCreationRequestDto videoCreationRequestDto){
        return ResponseEntity.ok(episodeService.createVideo(videoCreationRequestDto));
    }

}
