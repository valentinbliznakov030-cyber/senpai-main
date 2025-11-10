package bg.senpai_main.web;

import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.responses.CreatedAnimeResponse;
import bg.senpai_main.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {
    private final AnimeService animeService;


    @PostMapping
    public ResponseEntity<CreatedAnimeResponse> createAnime(@RequestBody AnimeInfoRequestDto dto) {
        Anime anime = animeService.createAnime(dto);

        CreatedAnimeResponse response = CreatedAnimeResponse.builder()
                .animeTitle(anime.getTitle())
                .episodeNumber(anime.getEpisodeNumber())
                .m3u8Link(anime.getM3u8Link())
                .build();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(anime.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/video")
    public ResponseEntity<VideoCreationResponseDto> createVideo(@RequestParam("m3u8Link") String m3u8Link){
        return ResponseEntity.ok(animeService.createVideo(m3u8Link));
    }

}
