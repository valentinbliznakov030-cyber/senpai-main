package bg.senpai_main.services;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteService {

    Favorite addToFavorites(Member member, Anime anime);

    void removeFromFavorites(Member member, Anime anime);

    boolean isFavorite(Member member, Anime anime);

    List<Favorite> getFavoritesByMember(Member member);

    void deleteById(UUID id);

    Page<Favorite> getFavoritesAnimesByMember(UUID memberId, int page, int size);

    Optional<Favorite> findById(UUID id);
}
