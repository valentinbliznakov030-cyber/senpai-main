package bg.senpai_main.services;

import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;

import java.util.Optional;
import java.util.UUID;

public interface AnimeService {
    Anime createAnime(AnimeInfoRequestDto animeInfoRequestDto);

    Optional<Anime> findByTitle(String animeName);
    Optional<Anime> findById(UUID animeId);

    Anime getAnime(AnimeInfoRequestDto dto);

    Optional<Anime> findByHiAnimeId(String hiAnimeId);
}
