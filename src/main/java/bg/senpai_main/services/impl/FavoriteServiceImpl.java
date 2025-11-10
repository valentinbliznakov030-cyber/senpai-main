package bg.senpai_main.services.impl;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final AnimeService animeService;

    @Override
    public Favorite addToFavorites(UUID memberId, FavoriteAddRequestDto favoriteAddRequestDto) {
        Anime anime = animeService.findByTitle(favoriteAddRequestDto.getAnimeTitle()).orElseThrow(() -> new EntityNotFoundException("Anime not found"));
        Member member = memberService.findById(memberId).orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (favoriteRepository.existsByMemberAndAnime(member, anime)) {
            throw new EntityAlreadyExistException("Anime already in favorites!");
        }


        Favorite favorite = Favorite.builder()
                .member(member)
                .anime(anime)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavourite(UUID favouriteId) {
        Favorite favorite = favoriteRepository.findById(favouriteId).orElseThrow(() -> new EntityNotFoundException("Favourite not found"));
        favoriteRepository.delete(favorite);
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


    public Page<Favorite> getFavoritesAnimesByMember(UUID memberId, Integer page, Integer size) {
        memberService.findById(memberId).orElseThrow(() -> new EntityNotFoundException("Member not found!"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return favoriteRepository.findByMember_Id(memberId, pageable);
    }


    @Override
    public Optional<Favorite> findById(UUID id) {
        return favoriteRepository.findById(id);
    }
}
