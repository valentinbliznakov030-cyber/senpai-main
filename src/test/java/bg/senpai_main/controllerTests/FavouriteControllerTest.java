package bg.senpai_main.controllerTests;

import bg.senpai_main.configs.JwtFilter;
import bg.senpai_main.configs.JwtUtil;
import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.mockutilities.WithMockMember;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.FavoriteService;
import bg.senpai_main.services.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(bg.senpai_main.web.FavouriteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FavouriteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;
    @MockBean
    private AnimeService animeService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtFilter wtFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockMember
    void shouldAddFavoriteSuccessfully() throws Exception {
        UUID animeUUID = UUID.randomUUID();
        UUID favouriteUUID = UUID.randomUUID();

        Anime anime = Anime.builder().id(animeUUID).build();
        Favorite favorite = Favorite.builder()
                .id(favouriteUUID)
                .anime(anime)
                .build();

        Mockito.when(favoriteService.addToFavorites(any(UUID.class), any(FavoriteAddRequestDto.class)))
                .thenReturn(favorite);

        FavoriteAddRequestDto request = new FavoriteAddRequestDto(animeUUID);

        mockMvc.perform(post("/api/v1/favourite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.id").value(favouriteUUID.toString()));
    }

    @Test
    @WithMockMember
    void shouldReturnFavoritesList() throws Exception {
        UUID favId = UUID.randomUUID();

        Anime anime = Anime.builder()
                .title("Black Clover")
                .build();

        Favorite favorite = Favorite.builder()
                .id(favId)
                .anime(anime)
                .build();

        Page<Favorite> page = new PageImpl<>(List.of(favorite), PageRequest.of(0, 15), 1);

        Mockito.when(favoriteService.getFavoritesAnimesByMember(nullable(UUID.class), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/favourite")
                        .param("page", "1")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animes[0].animeTitle").value("Black Clover"))
                .andExpect(jsonPath("$.animes[0].id").value(favId.toString()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.first").value(true));
    }


    @Test
    @WithMockUser
    void shouldDeleteFavorite() throws Exception {
        UUID favId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/favourite/" + favId))
                .andExpect(status().isNoContent());

        Mockito.verify(favoriteService).removeFavourite(any(UUID.class), favId);
    }
}
