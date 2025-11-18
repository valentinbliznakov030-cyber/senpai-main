package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.WatchHistoryResponseInfoDto;
import bg.senpai_main.entities.WatchHistory;
import bg.senpai_main.responses.WatchHistoryResponseDto;
import bg.senpai_main.services.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @GetMapping
    public ResponseEntity<?> getWatchHistory(
            @AuthenticationPrincipal MemberData memberData,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        // ⚡ Page<WatchHistory>
        Page<WatchHistory> historyPage =
                watchHistoryService.getHistory(memberData.getId(), page - 1, size);

        // ⚡ Превръщаме WatchHistory -> WatchHistoryResponseInfoDto
        List<WatchHistoryResponseInfoDto> infoList = historyPage.getContent().stream().map(entry ->
                        WatchHistoryResponseInfoDto.builder()
                                .watchHistoryId(entry.getId())
                                .animeTitle(entry.getEpisode().getAnime().getTitle())
                                .episodeNumber(entry.getEpisode().getEpisodeNumber())
                                .updatedOn(entry.getUpdatedOn())
                                .build()
                ).toList();

        // ⚡ Правим финалния DTO
        WatchHistoryResponseDto responseDto =
                new WatchHistoryResponseDto(historyPage, infoList);

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping()
    public ResponseEntity<Void> addToHistory(@AuthenticationPrincipal MemberData memberData, @RequestParam UUID episodeId) {
        watchHistoryService.addOrUpdateHistory(memberData.getId(), episodeId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(
            @AuthenticationPrincipal MemberData memberData,
            @PathVariable UUID id
    ) {
        watchHistoryService.delete(id, memberData.getId());
        return ResponseEntity.ok("History entry deleted");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearAllHistory(
            @AuthenticationPrincipal MemberData memberData
    ) {
        watchHistoryService.deleteAll(memberData.getId());
        return ResponseEntity.ok("All watch history cleared");
    }
}