package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.AnimeM3U8LinkDto;
import bg.senpai.common.dtos.VideoCreationRequestDto;
import bg.senpai.common.dtos.VideoCreationResponseDto;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.exceptions.AnimeVideoException;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimeServiceImpl implements AnimeService {
    private final AnimeRepository animeRepository;
    private final AnimeClient animeClient;
    private final CommentService commentService;
    private final MemberService memberService;


    public AnimeServiceImpl(AnimeRepository animeRepository, @Lazy CommentService commentService, MemberService memberService, AnimeClient animeClient) {
        this.animeRepository = animeRepository;
        this.commentService = commentService;
        this.memberService = memberService;
        this.animeClient = animeClient;
    }

    @Override
    public Optional<Anime> findByTitleAndEpisodeNumber(String animeTitle, int episodeNumber) {
        return animeRepository.findByTitleAndEpisodeNumber(animeTitle, episodeNumber);
    }

    @Override
    public String getM3U8Link(String animeUrl) {
        AnimeM3U8LinkDto animeM3U8LinkDto = animeClient.getM3u8Link(animeUrl);

        if (!animeM3U8LinkDto.isSuccess()) {
            throw new IllegalArgumentException(animeM3U8LinkDto.getMessage());
        }

        return animeM3U8LinkDto.getM3u8Link();
    }


    @Override
    public VideoCreationResponseDto createVideo(String m3u8Link) {
        String vidName = UUID.randomUUID().toString() + "_" +
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                        .format(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        VideoCreationRequestDto videoCreationRequestDto = VideoCreationRequestDto
                .builder()
                .m3u8Link(m3u8Link)
                .vidName(vidName)
                .build();

        VideoCreationResponseDto videoPathInfoDto = animeClient.createVideo(videoCreationRequestDto);

        if(!videoPathInfoDto.isSuccess()){
            throw new AnimeVideoException("Video not generated or found");
        }

        return videoPathInfoDto;

    }

    @Override
    public Optional<Anime> findById(UUID animeId) {
        return animeRepository.findById(animeId);
    }

    @Override
    public Anime createAnime(AnimeInfoRequestDto dto) {
        System.out.println(dto.getAnimeUrl());
        if(dto.getAnimeId() != null){
            Optional<Anime> existing = animeRepository.findById(dto.getAnimeId());

            if (existing.isPresent()) {
                return existing.get();
            }
        }

        if(findByTitleAndEpisodeNumber(dto.getAnimeTitle(), dto.getEpisodeNumber()).isPresent()){
            return findByTitleAndEpisodeNumber(dto.getAnimeTitle(), dto.getEpisodeNumber()).get();
        }


        String m3u8Link = getM3U8Link(dto.getAnimeUrl());

        Anime anime = Anime.builder()
                .title(URLDecoder.decode(dto.getAnimeTitle(), StandardCharsets.UTF_8))
                .episodeNumber(dto.getEpisodeNumber())
                .m3u8Link(m3u8Link)
                .build();

        return animeRepository.save(anime);
    }

    @Override
    public Optional<Anime> findByTitle(String animeTitle) {
        return animeRepository.findByTitle(animeTitle);
    }
}
