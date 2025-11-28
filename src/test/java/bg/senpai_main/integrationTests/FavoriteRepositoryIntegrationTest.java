package bg.senpai_main.integrationTests;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class FavoriteRepositoryIntegrationTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AnimeRepository animeRepository;

    private Member member;
    private Anime anime;

    @BeforeEach
    void initObjects(){
        member = new Member();
        member.setId(UUID.randomUUID());
        member.setUsername("Valka");
        member.setEmail("valka@example.com");
        member.setPassword("encodedPassword123");
        member.setRole(Role.USER);
        member.setActive(true);
        member.setRegisteredOn(LocalDateTime.now());
        member = memberRepository.save(member);

        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Demon Slayer");
        anime.setHiAnimeId(UUID.randomUUID().toString());
        anime = animeRepository.save(anime);
    }

    @Test
    void shouldSaveSuccessfully() {
        Favorite input = Favorite
                .builder()
                .member(member)
                .anime(anime)
                .build();

        Favorite saved = favoriteRepository.save(input);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMember().getId()).isEqualTo(member.getId());
        assertThat(saved.getAnime().getId()).isEqualTo(anime.getId());
    }

    @DisplayName("Shoud not save successfully because of the anime absence")
    @Test
    void shouldNotSaveSuccessfully(){
        Favorite favoriteToSave = Favorite
                .builder()
                .member(member)
                .build();

        assertThatThrownBy(() -> favoriteRepository.saveAndFlush(favoriteToSave));
    }

    @DisplayName("Should not save successfully because of the member absence")
    @Test
    void shouldNotSaveSuccessfullyWithoutMember(){
        Favorite favoriteToSave = Favorite
                .builder()
                .anime(anime)
                .build();

        assertThatThrownBy(() -> favoriteRepository.saveAndFlush(favoriteToSave));
    }


    @DisplayName("should find favourite by member successfully")
    @Test
    void shouldFindFavouriteByMemberSuccessfully(){
        Favorite favoriteToSave = Favorite.builder().member(member).anime(anime).build();

        favoriteRepository.save(favoriteToSave);

        List<Favorite> favourites = favoriteRepository.findByMember(member);

        assertThat(favourites).isNotNull();
        assertThat(favoriteToSave).isIn(favourites);
    }

    @DisplayName("does fav exist by member and anime")
    @Test
    void shouldCheckIfFavouriteExistsByMemberAndAnime(){
        Favorite favoriteToSave = Favorite.builder().member(member).anime(anime).build();

        favoriteRepository.saveAndFlush(favoriteToSave);

        boolean exists = favoriteRepository.existsByMemberAndAnime(member, anime);

        assertThat(exists).isTrue();
    }

    @DisplayName("does fav not exist by member and anime")
    @Test
    void shouldCheckIfFavouriteNotExistsByMemberAndAnime(){
        Member member = new Member();
        member.setId(UUID.randomUUID());
        member.setUsername("Shinra");
        member.setEmail("shinra@example.com");
        member.setPassword("superEncodedPass999");
        member.setRole(Role.ADMIN);
        member.setActive(false);
        member.setRegisteredOn(LocalDateTime.now().minusDays(3));
        member = memberRepository.save(member);

        Anime anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Fairy Tail");
        anime.setHiAnimeId(UUID.randomUUID().toString());
        anime = animeRepository.save(anime);

        Favorite favoriteToSave = Favorite.builder().member(this.member).anime(this.anime).build();

        favoriteRepository.saveAndFlush(favoriteToSave);

        boolean exists = favoriteRepository.existsByMemberAndAnime(member, anime);

        assertThat(exists).isFalse();
    }
}

