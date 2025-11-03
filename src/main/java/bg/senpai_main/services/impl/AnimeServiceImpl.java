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

import java.util.Optional;
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
    public Optional<Anime> findByTitle(String animeName) {
        return animeRepository.findByTitle(animeName);
    }

    public Resource streamAnime(AnimeInfoRequestDto dto) {
        Anime anime = animeRepository
                .findByTitleAndEpisodeNumber(dto.getAnimeTitle(), dto.getEpisodeNumber())
                .orElseThrow(() -> new IllegalArgumentException("Анимето не е намерено!"));

        AnimeStreamRequestDto streamRequest = AnimeStreamRequestDto.builder()
                .m3u8Link(anime.getM3u8Link())
                .build();

        // Викаме Anime микросървиса през FeignClient
        ResponseEntity<Resource> response = animeClient.streamAnime(streamRequest);
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
        return animeRepository.findByTitleAndEpisodeNumber(animeInfoRequestDto.getAnimeTitle(), animeInfoRequestDto.getEpisodeNumber());
    }


    @Override
    public Anime createAnime(AnimeInfoRequestDto dto) {
        Optional<Anime> existing = findByTitleAndEpisodeNumber(dto);

        if (existing.isPresent()) {
            return existing.get();
        }

        String m3u8Link = getM3U8Link(dto.getAnimeUrl());

        Anime anime = Anime.builder()
                .title(dto.getAnimeTitle())
                .episodeNumber(dto.getEpisodeNumber())
                .m3u8Link(m3u8Link)
                .build();

        return animeRepository.save(anime);
    }
}
