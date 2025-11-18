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
import feign.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimeServiceImpl implements AnimeService {
    private final AnimeRepository animeRepository;
    private final AnimeClient animeClient;
    private final CommentService commentService;
    private final MemberService memberService;
    private String sessionId = null;

    public AnimeServiceImpl(AnimeRepository animeRepository, @Lazy CommentService commentService, MemberService memberService, AnimeClient animeClient) {
        this.animeRepository = animeRepository;
        this.commentService = commentService;
        this.memberService = memberService;
        this.animeClient = animeClient;
    }

    @Override
    public Optional<Anime> findById(UUID animeId) {
        return animeRepository.findById(animeId);
    }

    @Override
    public Anime createAnime(AnimeInfoRequestDto dto) {
        System.out.println(dto.getAnimeTitle());

        Anime anime = Anime.builder()
                .title(dto.getAnimeTitle())
                .consumetAnimeId(dto.getAnimeConsumetAnimeId())
                .build();

        return animeRepository.save(anime);
    }

    @Override
    public Optional<Anime> findByConsumetAnimeId(String consumetAnimeId) {
        return animeRepository.findByConsumetAnimeId(consumetAnimeId);
    }


    @Override
    public Optional<Anime> findByTitle(String animeTitle) {
        return animeRepository.findByTitle(animeTitle);
    }
}
