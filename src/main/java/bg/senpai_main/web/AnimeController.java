package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.responses.CreatedAnimeResponse;
import bg.senpai_main.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/anime")
public class AnimeController {
    private final AnimeService animeService;

    @PostMapping
    public ResponseEntity<CreatedAnimeResponse> createAnime(@RequestBody AnimeInfoRequestDto animeInfoRequestDto) {
        Anime anime = animeService.createAnime(animeInfoRequestDto);

        // 🧩 създаваме URI към новия ресурс (примерно /api/anime/123)
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(anime.getId())
                .toUri();

        // 🧠 подготвяме DTO за отговор
        CreatedAnimeResponse response = CreatedAnimeResponse.builder()
                .animeTitle(anime.getTitle())
                .episodeNumber(anime.getEpisodeNumber())
                .m3u8Link(anime.getM3u8Link())
                .build();

        return ResponseEntity.created(uri).body(response);
    }


    @PostMapping("/stream")
    public ResponseEntity<Resource> streamAnime(@RequestBody AnimeInfoRequestDto dto) {
        Resource videoStream = animeService.streamAnime(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=anime.mp4")
                .contentType(MediaType.valueOf("video/mp4"))
                .body(videoStream);
    }


}
