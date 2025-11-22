package bg.senpai_main.controllerTests;

import bg.senpai_main.configs.JwtFilter;
import bg.senpai_main.configs.JwtUtil;
import bg.senpai_main.dtos.commentDtos.CommentAddRequestDto;
import bg.senpai_main.dtos.commentDtos.CommentChangeRequestDto;
import bg.senpai_main.entities.*;
import bg.senpai_main.enums.Role;
import bg.senpai_main.mockutilities.WithMockMember;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.EpisodeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(bg.senpai_main.web.CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock-ове
    @MockBean private CommentService commentService;
    @MockBean private AnimeService animeService;
    @MockBean private EpisodeService episodeService;
    @MockBean private MemberService memberService;
    @MockBean private JwtFilter jwtFilter;
    @MockBean private JwtUtil jwtUtil;


    // -------------------------------------------------------------
    // 🟣 1) Тест за успешно добавяне на коментар към епизод
    // -------------------------------------------------------------
    @Test
    @WithMockMember
    void shouldAddCommentSuccessfully() throws Exception {

        UUID commentId = UUID.randomUUID();
        UUID episodeId = UUID.randomUUID();

        Anime anime = Anime.builder()
                .id(UUID.randomUUID())
                .title("Naruto")
                .build();

        Episode episode = Episode.builder()
                .id(episodeId)
                .episodeNumber(1)
                .anime(anime)
                .build();

        Member member = Member.builder()
                .id(UUID.fromString("a1e3f8b0-4d5c-4e67-9c2b-3a0f5e1b7c8d"))
                .username("valkata")
                .email("valka@senpai.bg")
                .role(Role.USER)
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .episode(episode)
                .member(member)
                .content("Епизодът беше страхотен!")
                .build();

        CommentAddRequestDto request = new CommentAddRequestDto();
        request.setEpisodeId(episodeId);
        request.setContent("Епизодът беше страхотен!");

        Mockito.when(commentService.addComment(any(UUID.class), any(CommentAddRequestDto.class)))
                .thenReturn(comment);

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.content").value("Епизодът беше страхотен!"))
                .andExpect(jsonPath("$.commentId").value(commentId.toString()));
    }



    // -------------------------------------------------------------
    // 🟣 2) Тест за GET коментари за епизод
    // -------------------------------------------------------------
    @Test
    @WithMockMember
    void shouldReturnCommentsForEpisode() throws Exception {

        UUID episodeId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        Anime anime = Anime.builder()
                .id(UUID.randomUUID())
                .title("Naruto")
                .build();

        Episode episode = Episode.builder()
                .id(episodeId)
                .episodeNumber(10)
                .anime(anime)
                .build();

        Member member = Member.builder()
                .id(UUID.randomUUID())
                .username("Valka")
                .email("valka@senpai.bg")
                .role(Role.USER)
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .episode(episode)
                .member(member)
                .content("Много добър епизод!")
                .build();

        Page<Comment> page = new PageImpl<>(List.of(comment), PageRequest.of(0, 15), 1);

        Mockito.when(commentService.getCommentsForEpisode(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/comments")
                        .param("episodeId", episodeId.toString())
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].content").value("Много добър епизод!"))
                .andExpect(jsonPath("$.comments[0].commentCreator.username").value("Valka"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.first").value(true));
    }



    // -------------------------------------------------------------
    // 🟣 3) Тест за PATCH / промяна на коментар
    // -------------------------------------------------------------
    @Test
    @WithMockMember
    void shouldChangeCommentSuccessfully() throws Exception {
        UUID commentId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        Member member = Member.builder()
                .id(memberId)
                .username("Valka")
                .email("valka@senpai.bg")
                .role(Role.USER)
                .build();

        Comment updated = Comment.builder()
                .id(commentId)
                .member(member)
                .content("Редактиран коментар")
                .build();

        CommentChangeRequestDto requestDto = new CommentChangeRequestDto();
        requestDto.setCommentId(commentId);
        requestDto.setNewContent("Редактиран коментар");

        Mockito.when(commentService.updateComment(any(UUID.class), any(CommentChangeRequestDto.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(commentId.toString()))
                .andExpect(jsonPath("$.newContent").value("Редактиран коментар"));
    }

}
