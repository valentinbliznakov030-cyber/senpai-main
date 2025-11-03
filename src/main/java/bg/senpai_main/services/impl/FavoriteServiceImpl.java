package bg.senpai_main.services.impl;

import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.services.FavoriteService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    @Override
    public Favorite addToFavorites(Member member, Anime anime) {
        if (favoriteRepository.existsByMemberAndAnime(member, anime)) {
            throw new IllegalStateException("Anime already in favorites!");
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .anime(anime)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Override
    public void removeFromFavorites(Member member, Anime anime) {
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        Favorite existing = favorites.stream()
                .filter(f -> f.getAnime().equals(anime))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Favorite not found!"));
        favoriteRepository.delete(existing);
    }

    @Override
    public boolean isFavorite(Member member, Anime anime) {
        return favoriteRepository.existsByMemberAndAnime(member, anime);
    }

    @Override
    public List<Favorite> getFavoritesByMember(Member member) {
        return favoriteRepository.findByMember(member);
    }

    @Override
    public void deleteById(UUID id) {
        favoriteRepository.deleteById(id);
    }


    public Page<Favorite> getFavoritesAnimesByMember(UUID memberId, int page, int size) {
        memberService.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found!"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return favoriteRepository.findByMember_Id(memberId, pageable);
    }


    @Override
    public Optional<Favorite> findById(UUID id) {
        return favoriteRepository.findById(id);
    }
}
