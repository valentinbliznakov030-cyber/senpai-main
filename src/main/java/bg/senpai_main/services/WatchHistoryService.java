package bg.senpai_main.services;

import bg.senpai_main.entities.WatchHistory;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WatchHistoryService {
    // 🟣 Добавя или update-ва history при гледане
    void addOrUpdateHistory(UUID memberId, UUID episodeId);

    // 🟣 Връща списък с историята (paged)
    Page<WatchHistory> getHistory(UUID memberId, int page, int size);

    // 🟣 Връща броя на гледаните анимета
    long countWatched(UUID memberId);

    void delete(UUID historyId, UUID memberId);

    void deleteAll(UUID memberId);

}
