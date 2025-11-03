package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.AnimeM3U8LinkDto;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.dtos.AnimeStreamRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;

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

    public Resource streamAnime(AnimeInfoRequestDto dto) {
        Anime anime = animeRepository
                .findByTitleAndEpisodeNumber(dto.getAnimeTitle(), dto.getEpisodeNumber())
                .orElseThrow(() -> new IllegalArgumentException("Анимето не е намерено!"));

        String m3u8Link = anime.getM3u8Link();
        String sessionId = UUID.randomUUID().toString();

        ResponseEntity<Resource> response = animeClient.streamAnime(m3u8Link, sessionId);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new IllegalArgumentException("Video anime not found");
        }
        // Връщаме само самия поток (Resource)
        return response.getBody();
    }
    @Override
    public String getM3U8Link(String animeUrl){
        AnimeM3U8LinkDto animeM3U8LinkDto = animeClient.getM3u8Link(animeUrl);

        if(!animeM3U8LinkDto.isSuccess()){
            throw new IllegalArgumentException(animeM3U8LinkDto.getMessage());
        }

        return animeM3U8LinkDto.getM3u8Link();
    }

    @Override
    public Optional<Anime> findByTitleAndEpisodeNumber(AnimeInfoRequestDto animeInfoRequestDto) {
        String decodedAnimeTitle = URLDecoder.decode(animeInfoRequestDto.getAnimeTitle(), StandardCharsets.UTF_8);
        Integer episodeNumber = animeInfoRequestDto.getEpisodeNumber();

        System.out.println(decodedAnimeTitle);
        System.out.println(episodeNumber);

        return animeRepository.findByTitleAndEpisodeNumber(decodedAnimeTitle, episodeNumber);
    }


    @Override
    public Anime createAnime(AnimeInfoRequestDto dto) {
        System.out.println(dto.getAnimeUrl());
        Optional<Anime> existing = findByTitleAndEpisodeNumber(dto);

        if (existing.isPresent()) {
            return existing.get();
        }

        String m3u8Link = getM3U8Link(dto.getAnimeUrl());

        Anime anime = Anime.builder()
                .title(URLDecoder.decode(dto.getAnimeTitle(), StandardCharsets.UTF_8))
                .episodeNumber(dto.getEpisodeNumber())
                .m3u8Link(m3u8Link)
                .build();

        return animeRepository.save(anime);
    }
}
