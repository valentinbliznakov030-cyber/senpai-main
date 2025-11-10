package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.*;
import bg.senpai_main.entities.Favorite;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favourite")
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


            return FavoriteAnimeResponseInfoDto
                    .builder()
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
    public ResponseEntity<?> addFavAnime(@AuthenticationPrincipal MemberData memberData, @RequestBody FavoriteAddRequestDto favoriteAddRequestDto){
        Favorite favorite = favoriteService.addToFavorites(memberData.getId(), favoriteAddRequestDto);

        FavouriteAddResponseDto favouriteAddResponseDto = FavouriteAddResponseDto
                .builder()
                .animeTitle(favorite.getAnime().getTitle())
                .id(favorite.getId())
                .success(true)
                .statusCode(200)
                .build();
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(memberData.getId()).toUri();

        return ResponseEntity.created(location).body(favouriteAddResponseDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavourite(@PathVariable("id") UUID id){
        favoriteService.removeFavourite(id);
        return ResponseEntity.noContent().build();
    }

}
