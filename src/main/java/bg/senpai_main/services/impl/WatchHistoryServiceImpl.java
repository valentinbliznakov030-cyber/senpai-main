package bg.senpai_main.services.impl;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.entities.Member;
import bg.senpai_main.entities.WatchHistory;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.WatchHistoryRepository;
import bg.senpai_main.services.EpisodeService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchHistoryServiceImpl implements WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final MemberService memberService;
    private final EpisodeService episodeService;

    @Override
    public void addOrUpdateHistory(UUID memberId, UUID episodeId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Episode episode = episodeService.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException("Episode not found"));

        WatchHistory existing = watchHistoryRepository
                .findByMember_IdAndEpisode_Id(memberId, episodeId)
                .orElse(null);

        if (existing != null) {
            existing.setUpdatedOn(LocalDateTime.now());
            watchHistoryRepository.save(existing);
        } else {
            WatchHistory newEntry = WatchHistory.builder()
                    .member(member)
                    .episode(episode)
                    .updatedOn(LocalDateTime.now())
                    .build();

            watchHistoryRepository.save(newEntry);
        }
    }

    @Override
    public Page<WatchHistory> getHistory(UUID memberId, int page, int size) {
        return watchHistoryRepository.findByMember_Id(memberId, PageRequest.of(page, size));
    }

    @Override
    public long countWatched(UUID memberId) {
        return watchHistoryRepository.countByMember_Id(memberId);
    }

    @Override
    public void delete(UUID historyId, UUID memberId) {
        WatchHistory history = watchHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History entry not found"));

        if (history.getMember().getId().compareTo(memberId) != 0) {
            throw new RuntimeException("Unauthorized delete");
        }

        watchHistoryRepository.delete(history);
    }

    @Override
    public void deleteAll(UUID memberId) {
        watchHistoryRepository.deleteAllByMember_Id(memberId);
    }
}
