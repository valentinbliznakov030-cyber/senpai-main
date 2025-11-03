package bg.senpai_main.repositories;

import bg.senpai_main.entities.CommentLike;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {
    boolean existsByMemberAndComment(Member member, Comment comment);
    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);
}
