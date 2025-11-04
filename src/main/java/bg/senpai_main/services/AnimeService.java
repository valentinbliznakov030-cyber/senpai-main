package bg.senpai_main.services;

import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.Optional;

public interface AnimeService {
    String getM3U8Link(String url);
    Anime createAnime(AnimeInfoRequestDto animeInfoRequestDto);
    Optional<Anime> findByTitleAndEpisodeNumber(AnimeInfoRequestDto animeInfoRequestDto);
    Optional<Anime> findByTitleAndEpisodeNumber(String animeTitle, int episodeNumber);
    Resource streamAnime(String m3u8Link, String vodName);
}
