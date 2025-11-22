package bg.senpai_main.repositories;

import bg.senpai_main.entities.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, UUID> {
    Page<WatchHistory> findByMember_Id(UUID memberId, Pageable pageable);
    Optional<WatchHistory> findByMember_IdAndEpisode_Id(UUID memberId, UUID episodeId);
    long countByMember_Id(UUID memberId);

    void deleteAllByMember_Id(UUID memberId);
}
