package bg.senpai_main.services;

import bg.senpai_main.entities.WatchHistory;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WatchHistoryService {
    void addOrUpdateHistory(UUID memberId, UUID episodeId);
    Page<WatchHistory> getHistory(UUID memberId, int page, int size);
    long countWatched(UUID memberId);

    void delete(UUID historyId, UUID memberId);

    void deleteAll(UUID memberId);

}
