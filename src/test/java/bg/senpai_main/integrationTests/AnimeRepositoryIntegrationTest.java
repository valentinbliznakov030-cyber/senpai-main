package bg.senpai_main.integrationTests;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.repositories.AnimeRepository;
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
public class AnimeRepositoryIntegrationTest {

    @Autowired
    private AnimeRepository animeRepository;

    private Anime anime;

    @BeforeEach
    void setUp() {
        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Demon Slayer");
        anime.setHiAnimeId(UUID.randomUUID().toString());

        anime = animeRepository.save(anime);
    }

    @DisplayName("Should save anime successfully")
    @Test
    void shouldSaveAnimeSuccessfully() {
        Anime toSave = new Anime();
        toSave.setId(UUID.randomUUID());
        toSave.setTitle("Jujutsu Kaisen");
        toSave.setHiAnimeId(UUID.randomUUID().toString());

        Anime saved = animeRepository.save(toSave);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Jujutsu Kaisen");
    }

    @DisplayName("Should NOT save anime because title is missing")
    @Test
    void shouldNotSaveAnimeWithoutTitle() {
        Anime toSave = new Anime();
        toSave.setId(UUID.randomUUID());
        toSave.setHiAnimeId(UUID.randomUUID().toString());

        assertThatThrownBy(() -> animeRepository.saveAndFlush(toSave));
    }

    @DisplayName("Should NOT save anime because hiAnimeId is missing")
    @Test
    void shouldNotSaveAnimeWithoutHiAnimeId() {
        Anime toSave = new Anime();
        toSave.setId(UUID.randomUUID());
        toSave.setTitle("Naruto");

        assertThatThrownBy(() -> animeRepository.saveAndFlush(toSave));
    }

    @DisplayName("Should find anime by title successfully")
    @Test
    void shouldFindByTitleSuccessfully() {
        Optional<Anime> result = animeRepository.findByTitle("Demon Slayer");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(anime.getId());
    }

    @DisplayName("Should NOT find anime by non-existing title")
    @Test
    void shouldNotFindByTitle() {
        Optional<Anime> result = animeRepository.findByTitle("Non Existing Anime");

        assertThat(result).isNotPresent();
    }

    @DisplayName("Should find anime by hiAnimeId successfully")
    @Test
    void shouldFindByHiAnimeIdSuccessfully() {
        Optional<Anime> result = animeRepository.findByHiAnimeId(anime.getHiAnimeId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Demon Slayer");
    }

    @DisplayName("Should NOT find anime by invalid hiAnimeId")
    @Test
    void shouldNotFindByHiAnimeId() {
        Optional<Anime> result = animeRepository.findByHiAnimeId(UUID.randomUUID().toString());

        assertThat(result).isNotPresent();
    }
}
