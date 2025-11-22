package bg.senpai_main.services.impl;

import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.dtos.AnimeInfoRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
                .title(dto.getAnimeTitle().trim())
                .hiAnimeId(dto.getHiAnimeId().trim())
                .build();

        return animeRepository.save(anime);
    }

    @Override
    public Optional<Anime> findByTitle(String animeTitle) {
        return animeRepository.findByTitle(animeTitle);
    }


    @Override
    public Anime getAnime(AnimeInfoRequestDto dto) {
        return animeRepository.findByTitle(dto.getAnimeTitle().trim()).orElseGet(() -> createAnime(dto));
    }

    @Override
    public Optional<Anime> findByHiAnimeId(String hiAnimeId) {
        return animeRepository.findByHiAnimeId(hiAnimeId);
    }
}


