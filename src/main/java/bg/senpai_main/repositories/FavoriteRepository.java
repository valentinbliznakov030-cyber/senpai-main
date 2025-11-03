package bg.senpai_main.repositories;

import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.entities.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByMember(Member member);
    boolean existsByMemberAndAnime(Member member, Anime anime);
    Page<Favorite> findByMember_Id(UUID memberId, Pageable pageable);

}
