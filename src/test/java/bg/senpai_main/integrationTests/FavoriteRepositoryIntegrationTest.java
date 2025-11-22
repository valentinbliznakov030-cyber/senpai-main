package bg.senpai_main.integrationTests;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.repositories.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


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

    @Test
    void shouldSaveAndFindFavorite() {
        Member member = new Member();
        member.setId(UUID.randomUUID());
        member.setUsername("Valka");
        member.setEmail("valka@example.com");
        member.setPassword("encodedPassword123");
        member.setRole(Role.USER);
        member.setActive(true);
        member.setRegisteredOn(LocalDateTime.now());
        member = memberRepository.save(member);

        Anime anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Demon Slayer");
        anime = animeRepository.save(anime);

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setAnime(anime);

        Favorite saved = favoriteRepository.save(favorite);

        Favorite found = favoriteRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getMember().getUsername()).isEqualTo("Valka");
        assertThat(found.getAnime().getTitle()).isEqualTo("Demon Slayer");

        List<Favorite> favs = favoriteRepository.findByMember(member);
        assertThat(favs).hasSize(1);
    }
}

