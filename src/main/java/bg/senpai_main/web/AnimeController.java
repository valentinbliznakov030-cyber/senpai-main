package bg.senpai_main.web;

import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.responses.CreatedOrExistingAnimeResponse;
import bg.senpai_main.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {
    private final AnimeService animeService;

    @PostMapping
    public ResponseEntity<CreatedOrExistingAnimeResponse> createAnime(@RequestBody AnimeInfoRequestDto dto) {
        Optional<Anime> existing = animeService.findByConsumetAnimeId(dto.getAnimeConsumetAnimeId());
        CreatedOrExistingAnimeResponse response = null;
        Anime anime = null;

        anime = existing.orElseGet(() -> animeService.createAnime(dto));

        response = CreatedOrExistingAnimeResponse
                .builder()
                .animeId(anime.getId())
                .consumetAnimeId(anime.getConsumetAnimeId())
                .animeTitle(anime.getTitle())
                .build();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(anime.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }



}
