package bg.senpai_main.repositories;

import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByAnime(Anime anime, Pageable pageable);
}
