package bg.senpai_main.repositories;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, UUID> {
    boolean existsByEpisodeNumberAndAnime(Integer episodeNumber, Anime anime);
    Optional<Episode> findByEpisodeNumberAndAnime(Integer episodeNumber, Anime anime);
}
