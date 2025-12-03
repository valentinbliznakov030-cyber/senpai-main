package bg.senpai_main.integrationTests;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Episode;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.AnimeRepository;
import bg.senpai_main.repositories.CommentRepository;
import bg.senpai_main.repositories.EpisodeRepository;
import bg.senpai_main.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class CommentRepositoryIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    private Member member;
    private Anime anime;
    private Episode episode;

    @BeforeEach
    void init() {
        // Member
        member = new Member();
        member.setId(UUID.randomUUID());
        member.setUsername("Valka");
        member.setEmail("valka@example.com");
        member.setPassword("encodedPassword123");
        member.setRole(Role.USER);
        member.setActive(true);
        member.setRegisteredOn(LocalDateTime.now());
        member = memberRepository.save(member);

        // Anime
        anime = new Anime();
        anime.setId(UUID.randomUUID());
        anime.setTitle("Demon Slayer");
        anime.setHiAnimeId(UUID.randomUUID().toString());
        anime = animeRepository.save(anime);

        // Episode
        episode = new Episode();
        episode.setId(UUID.randomUUID());
        episode.setEpisodeNumber(1);
        episode.setM3u8Link("https://example.com/ep1.m3u8");
        episode.setAnime(anime);
        episode = episodeRepository.save(episode);
    }

    @DisplayName("Should save comment successfully")
    @Test
    void shouldSaveCommentSuccessfully() {
        Comment comment = Comment.builder()
                .content("Amazing episode!")
                .member(member)
                .episode(episode)
                .build();

        Comment saved = commentRepository.save(comment);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("Amazing episode!");
        assertThat(saved.getMember().getId()).isEqualTo(member.getId());
        assertThat(saved.getEpisode().getId()).isEqualTo(episode.getId());
    }

    @DisplayName("Should NOT save comment without content")
    @Test
    void shouldNotSaveCommentWithoutContent() {
        Comment comment = Comment.builder()
                .member(member)
                .episode(episode)
                .build();

        assertThatThrownBy(() -> commentRepository.saveAndFlush(comment));
    }

    @DisplayName("Should NOT save comment without member")
    @Test
    void shouldNotSaveCommentWithoutMember() {
        Comment comment = Comment.builder()
                .content("Nice ep")
                .episode(episode)
                .build();

        assertThatThrownBy(() -> commentRepository.saveAndFlush(comment));
    }

    @DisplayName("Should NOT save comment without episode")
    @Test
    void shouldNotSaveCommentWithoutEpisode() {
        Comment comment = Comment.builder()
                .content("Nice ep")
                .member(member)
                .build();

        assertThatThrownBy(() -> commentRepository.saveAndFlush(comment));
    }

    @DisplayName("Should find comments by episode with paging")
    @Test
    void shouldFindCommentsByEpisodeWithPaging() {
        Comment c1 = commentRepository.save(
                Comment.builder().content("1").member(member).episode(episode).build()
        );
        Comment c2 = commentRepository.save(
                Comment.builder().content("2").member(member).episode(episode).build()
        );

        Page<Comment> page = commentRepository.findByEpisode(
                episode,
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting("content").contains("1", "2");
    }

    @DisplayName("Should return empty page for episode without comments")
    @Test
    void shouldReturnEmptyPageWhenNoCommentsExist() {
        // Create another episode with no comments
        Episode ep2 = Episode.builder()
                .id(UUID.randomUUID())
                .episodeNumber(99)
                .m3u8Link("https://example.com/ep99.m3u8")
                .anime(anime)
                .build();

        ep2 = episodeRepository.save(ep2);

        Page<Comment> page = commentRepository.findByEpisode(
                ep2,
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getContent()).isEmpty();
    }
}
