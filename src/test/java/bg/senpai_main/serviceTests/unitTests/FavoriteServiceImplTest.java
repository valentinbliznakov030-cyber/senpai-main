package bg.senpai_main.serviceTests.unitTests;
import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityAlreadyExistException;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private AnimeService animeService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Member member;
    private Anime anime;
    private FavoriteAddRequestDto request;

    @BeforeEach
    void setup() {
        member = new Member();
        member.setId(UUID.randomUUID());

        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Naruto");

        request = new FavoriteAddRequestDto();
        request.setAnimeId(UUID.randomUUID());
    }

    @Test
    void addToFavorites_shouldSaveNewFavorite_whenNotExists() {
        // Arrange
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        when(animeService.findById(any(UUID.class))).thenReturn(Optional.of(anime));
        when(favoriteRepository.existsByMemberAndAnime(member, anime)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Favorite result = favoriteService.addToFavorites(member.getId(), request);

        // Assert
        assertThat(result)
                .isNotNull()
                .extracting(Favorite::getAnime, Favorite::getMember)
                .containsExactly(anime, member);

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_shouldThrowException_whenAlreadyExists() {
        // Arrange
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        when(animeService.findByTitle("Naruto")).thenReturn(Optional.of(anime));
        when(favoriteRepository.existsByMemberAndAnime(member, anime)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> favoriteService.addToFavorites(member.getId(), request))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining("Anime already in favorites!");
    }
}
