package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.AnimeM3U8LinkDto;
import bg.senpai.common.dtos.VideoCreationRequestDto;
import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.dtos.EpisodeCreationRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.exceptions.AnimeVideoException;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.EpisodeRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {
    private final AnimeClient animeClient;
    private final AnimeService animeService;
    private final EpisodeRepository episodeRepository;

    @Override
    public Optional<Episode> findById(UUID episodeId) {
        return episodeRepository.findById(episodeId);
    }

    @Override
    public Episode createEpisode(EpisodeCreationRequestDto episodeCreationRequestDto, String sessionId) {
        String episodeUrl = episodeCreationRequestDto.getEpisodeUrl();
        Integer episodeNumber = episodeCreationRequestDto.getEpisodeNumber();
        String consumetAnimeId = episodeCreationRequestDto.getConsumetAnimeId();
        Anime anime = animeService.findByConsumetAnimeId(consumetAnimeId).orElseThrow(() -> new EntityNotFoundException("Anime not found"));

        String m3u8Link = getM3U8Link(episodeUrl, sessionId);

        Episode episode = Episode.builder()
                .episodeNumber(episodeNumber)
                .anime(anime)
                .m3u8Link(m3u8Link)
                .build();

        return episodeRepository.save(episode);
    }

    @Override
    public String getM3U8Link(String animeUrl, String sessionId) {

        ResponseEntity<AnimeM3U8LinkDto> response =
                animeClient.getM3u8Link(animeUrl, sessionId);

        AnimeM3U8LinkDto body = response.getBody();

        if (body == null || !body.isSuccess()) {
            throw new IllegalArgumentException("M3U8Link not found!");
        }

        return body.getM3u8Link();
    }

    @Override
    public VideoCreationResponseDto createVideo(VideoCreationRequestDto videoCreationRequestDto) {

        ResponseEntity<VideoCreationResponseDto> videoCreationResponse = animeClient.createVideo(videoCreationRequestDto);

        if( videoCreationResponse.getStatusCode().value() != 201 || !Objects.requireNonNull(videoCreationResponse.getBody()).isSuccess()){
            throw new AnimeVideoException("Video not generated or found");
        }

        return videoCreationResponse.getBody();

    }


    @Override
    public Optional<Episode> findByEpisodeNumberAndAnime(Integer episodeNumber, Anime anime) {
        return episodeRepository.findByEpisodeNumberAndAnime(episodeNumber, anime);
    }

    @Override
    public Optional<Episode> findByEpisodeNumberAndConsumetAnimeId(Integer episodeNumber, String consumetAnimeId) {
        Anime anime = animeService.findByConsumetAnimeId(consumetAnimeId).orElseThrow(() -> new EntityNotFoundException("Anime not found!"));

        return episodeRepository.findByEpisodeNumberAndAnime(episodeNumber, anime);
    }
}
