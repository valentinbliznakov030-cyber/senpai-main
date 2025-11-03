package bg.senpai_main.web;

import bg.senpai_main.services.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment-like")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;


}
