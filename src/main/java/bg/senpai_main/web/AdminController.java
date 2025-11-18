package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.entities.Comment;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.services.CommentService;
import bg.senpai_main.services.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers(){
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/member/filter")
    public ResponseEntity<List<Member>> filterMembers(
            @RequestParam(required = false, value = "username") String username,
            @RequestParam(required = false, value = "email") String email,
            @RequestParam(required = false, value = "active") Boolean active,
            @RequestParam(required = false, value = "role") Role role,
            @RequestParam(required = false, value = "registeredOn")LocalDateTime localDateTime){
        return ResponseEntity.ok(memberService.findFilteredMembers(username, email, active, role, localDateTime));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(){
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("/comment/filter")
    public ResponseEntity<List<Comment>> filterComments(
            @RequestParam(required = false, value = "commentId") UUID commentId,
            @RequestParam(required = false, value = "content") String content,
            @RequestParam(required = false, value = "createdOn") LocalDateTime createdOn,
            @RequestParam(required = false, value = "updatedOn") LocalDateTime updatedOn,

            @RequestParam(required = false, value = "username") String username,

            @RequestParam(required = false, value = "animeId") String animeId,
            @RequestParam(required = false, value = "animeTitle") String animeTitle

    ) {


        return ResponseEntity.ok(commentService.findFilteredComments(
                commentId,
                content,
                username,
                animeId,
                animeTitle,
                createdOn,
                updatedOn
        ));
    }


}
