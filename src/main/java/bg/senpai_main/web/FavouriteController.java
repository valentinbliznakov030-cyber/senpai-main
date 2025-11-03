package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.FavoriteAddRequest;
import bg.senpai_main.dtos.FavoriteAnimeResponseInfoDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.Member;
import bg.senpai_main.responses.FavoriteAnimeResponseDto;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.FavoriteService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/favourite")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavoriteService favoriteService;
    private final AnimeService animeService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getFavAnime(@AuthenticationPrincipal MemberData memberData, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size){
        Page<Favorite> favorites = favoriteService.getFavoritesAnimesByMember(memberData.getId(), page - 1 , size);

        List<FavoriteAnimeResponseInfoDto> animeList = favorites.getContent().stream().map(favorite -> {
            UUID id = favorite.getId();
            String animeTitle = favorite.getAnime().getTitle();
            String animeM3u8Link = favorite.getAnime().getM3u8Link();


            return FavoriteAnimeResponseInfoDto
                    .builder()
                    .animeM3u8Link(animeM3u8Link)
                    .animeTitle(animeTitle)
                    .id(id)
                    .build();
        }).toList();

        FavoriteAnimeResponseDto responseDto = new FavoriteAnimeResponseDto(
                favorites,
                animeList
        );

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFavAnime(@PathVariable UUID id){
        return ResponseEntity.ok(favoriteService.findById(id).orElseThrow(() -> new IllegalArgumentException("Fav anime not found")));
    }

    @PostMapping
    public ResponseEntity<?> addFavAnime(@AuthenticationPrincipal MemberData memberData, @RequestBody FavoriteAddRequest favoriteAddRequest){
        Anime anime = animeService.findByTitle(favoriteAddRequest.getAnimeName()).orElseThrow(() -> new IllegalArgumentException("Anime not found"));
        Member member = memberService.findById(memberData.getId()).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Favorite favorite = favoriteService.addToFavorites(member, anime);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(member.getId()).toUri();

        return ResponseEntity.created(location).body(favorite);

    }



}
