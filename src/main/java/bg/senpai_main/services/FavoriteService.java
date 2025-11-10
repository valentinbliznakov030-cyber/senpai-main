package bg.senpai_main.services;

import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteService {

    Favorite addToFavorites(UUID memberId, FavoriteAddRequestDto favoriteAddRequestDto);

    void removeFavourite(UUID favouriteId);

    boolean isFavorite(Member member, Anime anime);

    List<Favorite> getFavoritesByMember(Member member);

    void deleteById(UUID id);

    Page<Favorite> getFavoritesAnimesByMember(UUID memberId, Integer page, Integer size);

    Optional<Favorite> findById(UUID id);
}
