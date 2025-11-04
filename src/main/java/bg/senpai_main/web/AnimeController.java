package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.responses.CreatedAnimeResponse;
import bg.senpai_main.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {
    private final AnimeService animeService;

    @PostMapping
    public ResponseEntity<CreatedAnimeResponse> createAnime(@RequestBody AnimeInfoRequestDto dto) {
        Anime anime = animeService.createAnime(dto);

        String vidName = UUID.randomUUID().toString() + Date.from(Instant.now()).toString();

        CreatedAnimeResponse response = CreatedAnimeResponse.builder()
                .animeTitle(anime.getTitle())
                .episodeNumber(anime.getEpisodeNumber())
                .m3u8Link(anime.getM3u8Link())
                .vidName(vidName)
                .build();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(anime.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }


    @GetMapping(value = "/stream", produces = "video/mp4")
    public ResponseEntity<ResourceRegion> streamAnime(
            @RequestParam("m3u8Link") String m3u8Link,
            @RequestParam("sessionId") String sessionId,
            @RequestHeader HttpHeaders headers) {

        Resource videoStream = animeService.streamAnime(m3u8Link, sessionId);

        try {
            long contentLength = videoStream.contentLength();

            HttpRange range = headers.getRange().isEmpty()
                    ? null
                    : headers.getRange().get(0);

            ResourceRegion region;
            if (range != null) {
                long start = range.getRangeStart(contentLength);
                long end = range.getRangeEnd(contentLength);
                long rangeLength = Math.min(1_000_000, end - start + 1);
                region = new ResourceRegion(videoStream, start, rangeLength);
            } else {
                long rangeLength = Math.min(1_000_000, contentLength);
                region = new ResourceRegion(videoStream, 0, rangeLength);
            }

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaTypeFactory.getMediaType(videoStream)
                            .orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"anime.mp4\"")
                    .body(region);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
