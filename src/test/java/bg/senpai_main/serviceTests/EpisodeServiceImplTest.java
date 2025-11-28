package bg.senpai_main.serviceTests;
import bg.senpai.common.dtos.AnimeM3U8LinkDto;
import bg.senpai.common.dtos.VideoCreationRequestDto;
import bg.senpai_main.clients.AnimeClient;
import bg.senpai_main.dtos.EpisodeCreationRequestDto;
import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityAlreadyExistException;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.EpisodeRepository;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.FavoriteService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.impl.EpisodeServiceImpl;
import bg.senpai_main.services.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EpisodeServiceImplTest {
    @Mock
    private AnimeService animeService;
    @Mock
    private EpisodeRepository episodeRepository;
    @Mock
    private AnimeClient animeClient;
    @InjectMocks
    private EpisodeServiceImpl episodeService;
    @Captor
    private ArgumentCaptor<Episode> episodeCaptor;


    private Anime anime;
    private VideoCreationRequestDto videoCreationRequestDto;
    @BeforeEach
    void initObjects(){
        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setHiAnimeId("https://demon-slayer-48");
        anime.setTitle("Demon Slayer");

        videoCreationRequestDto = new VideoCreationRequestDto();
        videoCreationRequestDto.setM3u8Link("https://master.m3u8");
        videoCreationRequestDto.setVidName("session123");


    }

    @DisplayName("shoud create successfuly episode")
    @Test
    void shouldCreateEpisode(){
        String sessionId = "session123";

        AnimeM3U8LinkDto animeM3U8LinkDto = new AnimeM3U8LinkDto();
        animeM3U8LinkDto.setM3u8Link("https://master.m3u8");
        animeM3U8LinkDto.setSuccess(true);

        EpisodeCreationRequestDto episodeCreationRequestDto = new EpisodeCreationRequestDto();
        episodeCreationRequestDto.setAnimeId(anime.getId());
        episodeCreationRequestDto.setEpisodeNumber(1);
        episodeCreationRequestDto.setEpisodeUrl("https://animepahe.si/some-anime");

        Episode expected = Episode.builder()
                .id(UUID.randomUUID())
                .episodeNumber(episodeCreationRequestDto.getEpisodeNumber())
                .m3u8Link(animeM3U8LinkDto.getM3u8Link())
                .anime(anime)
                .build();

        when(animeService.findById(anime.getId())).thenReturn(Optional.of(anime));
        when(animeClient.getM3u8Link(episodeCreationRequestDto.getEpisodeUrl(), sessionId))
                .thenReturn(new ResponseEntity<>(animeM3U8LinkDto, HttpStatusCode.valueOf(200)));
        when(episodeRepository.save(any(Episode.class))).thenReturn(expected);

        Episode actual = episodeService.createEpisode(episodeCreationRequestDto, sessionId);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        verify(animeClient, times(1)).getM3u8Link(episodeCreationRequestDto.getEpisodeUrl(), sessionId);
        verify(animeService, times(1)).findById(episodeCreationRequestDto.getAnimeId());
        verify(episodeRepository, times(1)).save(episodeCaptor.capture());

        Episode passed = episodeCaptor.getValue();
        assertThat(passed.getEpisodeNumber()).isEqualTo(1);
        assertThat(passed.getM3u8Link()).isEqualTo("https://master.m3u8");
        assertThat(passed.getAnime()).isEqualTo(anime);
    }
}
