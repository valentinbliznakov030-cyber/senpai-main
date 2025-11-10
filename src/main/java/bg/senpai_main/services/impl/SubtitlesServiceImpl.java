package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.SubscriptionStatusDTO;
import bg.senpai.common.dtos.SubtitlesDownloadRequestDto;
import bg.senpai.common.dtos.SubtitlesDownloadedResponseDto;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.clients.SubscriptionClient;
import bg.senpai_main.exceptions.SubtitleAccessDeniedException;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.SubtitlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubtitlesServiceImpl implements SubtitlesService {
    private final AnimeClient animeClient;
    private final SubscriptionClient subscriptionClient;

    @Override
    public SubtitlesDownloadedResponseDto downloadSubtitles(UUID memberId, SubtitlesDownloadRequestDto subtitlesDownloadRequestDto ) {
        SubscriptionStatusDTO status = subscriptionClient.getStatus(memberId);

        if (status.getPlanType().equals("FREE") && status.isLimitReached()) {
            throw new SubtitleAccessDeniedException("Download limit reached for free plan");
        }


        return animeClient.downloadSubtitles(subtitlesDownloadRequestDto); // външното API
    }
}
