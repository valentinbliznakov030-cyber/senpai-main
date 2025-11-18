package bg.senpai_main.services;

import bg.senpai.common.dtos.VideoCreationRequestDto;
import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.dtos.EpisodeCreationRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;

import java.util.Optional;
import java.util.UUID;

public interface EpisodeService {
    Episode createEpisode(EpisodeCreationRequestDto episodeCreationRequestDto, String sessionId);
    VideoCreationResponseDto createVideo(VideoCreationRequestDto videoCreationRequestDto);
    String getM3U8Link(String url, String sessionId);
    Optional<Episode> findByEpisodeNumberAndAnime(Integer episodeNumber, Anime anime);
    Optional<Episode> findByEpisodeNumberAndConsumetAnimeId(Integer episodeNumber, String consumetAnimeId);
    Optional<Episode> findById(UUID episodeId);

}
