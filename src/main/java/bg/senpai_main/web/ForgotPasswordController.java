package bg.senpai_main.web;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.forgotPasswordDtos.ForgotPasswordRequestDto;
import bg.senpai_main.dtos.forgotPasswordDtos.PasswordResetRequestDto;
import bg.senpai_main.services.ForgotPasswordService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<?> sendCode(@AuthenticationPrincipal MemberData memberData, @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
        forgotPasswordService.sendResetCode(forgotPasswordRequestDto.getEmail());
        return ResponseEntity.ok(Map.of("sent", true));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody PasswordResetRequestDto passwordResetRequestDto){
        return ResponseEntity.ok(
                forgotPasswordService.verifyCode(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getCode())?
                        Map.of("verified", true) : Map.of("verified", false)
        );
    }
}
