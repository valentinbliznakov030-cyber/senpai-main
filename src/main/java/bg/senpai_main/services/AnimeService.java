package bg.senpai_main.services;

import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import org.springframework.core.io.Resource;

import java.util.Optional;
import java.util.UUID;

public interface AnimeService {
    Anime createAnime(AnimeInfoRequestDto animeInfoRequestDto);
    Optional<Anime> findByConsumetAnimeId(String consumetAnimeId);

    Optional<Anime> findByTitle(String animeName);
    Optional<Anime> findById(UUID animeId);
}
