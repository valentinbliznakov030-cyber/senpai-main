package bg.senpai_main.services.impl;

import bg.senpai.common.dtos.*;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.clients.SubscriptionClient;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.exceptions.SubtitleAccessDeniedException;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.SubtitlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubtitlesServiceImpl implements SubtitlesService {
    private final AnimeClient animeClient;
    private final SubscriptionClient subscriptionClient;

    @Override
    public TranslateSubtitleResponseDto translateSubtitles(UUID memberId, SubtitlesDownloadRequestDto subtitlesDownloadRequestDto ) {
        SubscriptionStatusDTO status = subscriptionClient.getStatus(memberId);

        if (status.getPlanType().equals("FREE") && status.isLimitReached()) {
            throw new SubtitleAccessDeniedException("Download limit reached for free plan");
        }

        ResponseEntity<SubtitlesDownloadedResponseDto> response = animeClient.downloadSubtitles(subtitlesDownloadRequestDto);
        if(!Objects.requireNonNull(response.getBody()).isSuccess()){
            throw new EntityNotFoundException("Subtitle not found");
        }

        TranslateSubtitleRequestDto translateSubtitleRequestDto = TranslateSubtitleRequestDto
                .builder()
                .subtitleName(response.getBody().getSubtitleName())
                .build();

        ResponseEntity<TranslateSubtitleResponseDto> translationResponse = animeClient.translateSubtitles(translateSubtitleRequestDto);

        if(!Objects.requireNonNull(translationResponse.getBody()).isSuccess()){
            throw new RuntimeException("Couldn't transalte");
        }

        subscriptionClient.incrementWatch(memberId);
        return translationResponse.getBody();
    }
}
