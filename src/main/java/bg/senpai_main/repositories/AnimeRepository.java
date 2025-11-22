package bg.senpai_main.repositories;

import bg.senpai_main.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnimeRepository extends JpaRepository<Anime, UUID> {
    Optional<Anime> findByTitle(String title);

    Optional<Anime> findByHiAnimeId(String hiAnimeId);
}
