package bg.senpai_main.serviceTests;
import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityAlreadyExistException;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.FavoriteService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;

import java.util.List;
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
        anime.setHiAnimeId("https://demon-slayer-48");
        anime.setTitle("Naruto");

        request = new FavoriteAddRequestDto();
        request.setAnimeId(UUID.randomUUID());
    }

    @Test
    void addToFavorites_shouldSaveNewFavorite_whenNotExists() {
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        when(animeService.findById(any(UUID.class))).thenReturn(Optional.of(anime));
        when(favoriteRepository.existsByMemberAndAnime(member, anime)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(i -> i.getArgument(0));

        Favorite result = favoriteService.addToFavorites(member.getId(), request);

        assertThat(result)
                .isNotNull()
                .extracting(Favorite::getAnime, Favorite::getMember)
                .containsExactly(anime, member);

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_shouldThrowException_whenAlreadyExists() {
        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        when(animeService.findByTitle("Naruto")).thenReturn(Optional.of(anime));
        when(favoriteRepository.existsByMemberAndAnime(member, anime)).thenReturn(true);

        assertThatThrownBy(() -> favoriteService.addToFavorites(member.getId(), request))
                .isInstanceOf(EntityAlreadyExistException.class)
                .hasMessageContaining("Anime already in favorites!");
    }

    @DisplayName("should remove from favourite when favourite exist")
    @Test
    void shouldRemoveFavouriteIfItExists(){
        UUID favouriteId = UUID.randomUUID();
        Favorite favorite = Favorite.builder().anime(anime).member(member).id(favouriteId).build();

        when(favoriteRepository.findById(favouriteId)).thenReturn(Optional.of(favorite));

        favoriteService.removeFavourite(member.getId(), favouriteId);

        verify(favoriteRepository).delete(favorite);
    }

    @DisplayName("should not remove from favourite when favourite doesnt exist")
    @Test
    void shouldNotRemoveFavouriteIfItNotExists(){
        UUID favouriteId = UUID.randomUUID();

        when(favoriteRepository.findById(favouriteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                favoriteService.removeFavourite(member.getId(), favouriteId)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Favourite not found");

        verify(favoriteRepository).findById(favouriteId);
        verify(favoriteRepository, never()).delete(any());
    }

    @DisplayName("shoud return favourites of member who exist")
    @Test
    void shouldReturnFavouriteByMember(){
        Favorite favorite = Favorite.builder().anime(anime).member(member).id(UUID.randomUUID()).build();
        Page<Favorite> page = new PageImpl<>(List.of(favorite), PageRequest.of(1, 5, Sort.by("id").descending()), 1);

        when(memberService.findById(member.getId())).thenReturn(Optional.of(member));
        when(favoriteRepository.findByMember_Id(eq(member.getId()), any(Pageable.class))).thenReturn(page);

        Page<Favorite> resultPage = favoriteService.getFavoritesAnimesByMember(member.getId(), 1, 5);

        assertThat(resultPage.getContent()).isNotEmpty();
        verify(memberService, times(1)).findById(member.getId());
        verify(favoriteRepository, times(1)).findByMember_Id(eq(member.getId()), any(Pageable.class));
    }

    @DisplayName("shoud not return favourites of member who exist")
    @Test
    void shouldNotReturnFavouriteByMember(){
        when(memberService.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                favoriteService.getFavoritesAnimesByMember(member.getId(), 1, 5)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Member not found!");
        verify(memberService, times(1)).findById(any(UUID.class));
    }
}
