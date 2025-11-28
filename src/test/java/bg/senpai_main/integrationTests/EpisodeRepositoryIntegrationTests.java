package bg.senpai_main.integrationTests;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;

import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.repositories.EpisodeRepository;
import bg.senpai_main.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class EpisodeRepositoryIntegrationTests {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Anime anime;

    @BeforeEach
    void setUp() {
        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Demon Slayer");
        anime.setHiAnimeId(UUID.randomUUID().toString());
        anime = animeRepository.save(anime);
    }

    @DisplayName("Should save episode successfully")
    @Test
    void shouldSaveEpisodeSuccessfully() {
        Episode episode = Episode.builder()
                .anime(anime)
                .m3u8Link("https://example.com/ep1.m3u8")
                .episodeNumber(1)
                .build();

        Episode saved = episodeRepository.save(episode);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAnime().getId()).isEqualTo(anime.getId());
        assertThat(saved.getEpisodeNumber()).isEqualTo(1);
    }

    @DisplayName("Should NOT save episode because of missing anime")
    @Test
    void shouldNotSaveEpisodeWithoutAnime() {
        Episode episode = Episode.builder()
                .episodeNumber(1)
                .m3u8Link("https://example.com/ep1.m3u8")
                .build();

        assertThatThrownBy(() -> episodeRepository.saveAndFlush(episode));
    }

    @DisplayName("Should NOT save episode because of missing m3u8Link")
    @Test
    void shouldNotSaveEpisodeWithoutM3U8Link() {
        Episode episode = Episode.builder()
                .anime(anime)
                .episodeNumber(1)
                .build();

        assertThatThrownBy(() -> episodeRepository.saveAndFlush(episode));
    }

    @DisplayName("Should NOT save episode because of missing episodeNumber")
    @Test
    void shouldNotSaveEpisodeWithoutEpisodeNumber() {
        Episode episode = Episode.builder()
                .anime(anime)
                .m3u8Link("https://example.com/ep1.m3u8")
                .build();

        assertThatThrownBy(() -> episodeRepository.saveAndFlush(episode));
    }

    @DisplayName("Should find episode by episodeNumber and anime successfully")
    @Test
    void shouldFindEpisodeByEpisodeNumberAndAnime() {
        Episode episode = Episode.builder()
                .anime(anime)
                .m3u8Link("https://example.com/ep2.m3u8")
                .episodeNumber(2)
                .build();

        episodeRepository.save(episode);

        Optional<Episode> found = episodeRepository.findByEpisodeNumberAndAnime(2, anime);

        assertThat(found).isPresent();
        assertThat(found.get().getEpisodeNumber()).isEqualTo(2);
        assertThat(found.get().getAnime().getId()).isEqualTo(anime.getId());
    }

    @DisplayName("Should NOT find episode when episodeNumber and anime do not match")
    @Test
    void shouldNotFindEpisodeByEpisodeNumberAndAnime() {
        Episode ep = Episode.builder()
                .anime(anime)
                .m3u8Link("https://example.com/ep1.m3u8")
                .episodeNumber(1)
                .build();

        episodeRepository.save(ep);

        Optional<Episode> result = episodeRepository.findByEpisodeNumberAndAnime(99, anime);

        assertThat(result).isNotPresent();
    }
}
