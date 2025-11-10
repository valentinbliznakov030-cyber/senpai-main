package bg.senpai_main.controllerTests;

import bg.senpai_main.configs.JwtFilter;
import bg.senpai_main.configs.JwtUtil;
import bg.senpai_main.dtos.commentDtos.CommentAddRequestDto;
import bg.senpai_main.dtos.commentDtos.CommentChangeRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.mockutilities.WithMockMember;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(bg.senpai_main.web.CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    // други зависимости от контролера
    @MockBean
    private AnimeService animeService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockMember
    void shouldAddCommentSuccessfully() throws Exception {
        // Тест за POST /api/v1/comments

        UUID commentId = UUID.randomUUID();
        UUID animeId = UUID.randomUUID();

        // подготвяме фалшив аниме и коментар
        Anime anime = Anime.builder().id(animeId).title("Naruto").build();
        Member member = Member.builder()
                .id(UUID.fromString("a1e3f8b0-4d5c-4e67-9c2b-3a0f5e1b7c8d"))
                .username("valkata")
                .email("valka@senpai.bg")
                .role(Role.USER)
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .anime(anime)
                .member(member)
                .content("Епизодът беше страхотен!")
                .build();

        CommentAddRequestDto request = new CommentAddRequestDto();
        request.setAnimeId(animeId);
        request.setContent("Епизодът беше страхотен!");

        // мокваме сървиса
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

    @Test
    @WithMockMember
    void shouldReturnCommentsForAnime() throws Exception {
        UUID animeId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        Member member = Member.builder()
                .id(UUID.randomUUID())
                .username("Valka")
                .email("valka@senpai.bg")
                .role(Role.USER)
                .build();

        Anime anime = Anime.builder().id(animeId).title("Naruto").build();

        Comment comment = Comment.builder()
                .id(commentId)
                .anime(anime)
                .member(member)
                .content("Много добро аниме!")
                .build();

        Page<Comment> page = new PageImpl<>(List.of(comment), PageRequest.of(0, 15), 1);

        Mockito.when(commentService.getCommentsForAnime(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/comments")
                        .param("animeId", animeId.toString())
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].content").value("Много добро аниме!"))
                .andExpect(jsonPath("$.comments[0].commentCreator.username").value("Valka"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.first").value(true));
    }

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

        Comment originalComment = Comment.builder()
                .id(commentId)
                .content("Първоначален коментар")
                .member(member)
                .build();

        Comment updatedComment = Comment.builder()
                .id(commentId)
                .content("Редактиран коментар")
                .member(member)
                .build();

        CommentChangeRequestDto requestDto = new CommentChangeRequestDto();
        requestDto.setCommentId(commentId);
        requestDto.setNewContent("Редактиран коментар");

        Mockito.when(commentService.updateComment(any(UUID.class), any(CommentChangeRequestDto.class)))
                .thenReturn(updatedComment);

        mockMvc.perform(patch("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(commentId.toString()))
                .andExpect(jsonPath("$.newContent").value("Редактиран коментар"));
    }



}
