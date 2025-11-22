package bg.senpai_main.web;

import bg.senpai_main.dtos.forgotPasswordDtos.ChangePasswordRequestDto;
import bg.senpai_main.dtos.forgotPasswordDtos.ForgotPasswordRequestDto;
import bg.senpai_main.dtos.forgotPasswordDtos.VerifyCodeRequestDto;
import bg.senpai_main.entities.ForgotPasswordToken;
import bg.senpai_main.services.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> sendCode(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
        ForgotPasswordToken token = forgotPasswordService.sendResetCode(forgotPasswordRequestDto.getEmail());
        return ResponseEntity.ok(Map.of(
                "sent", true,
                "timeExp",token.getExpiration())
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return ResponseEntity.ok(
                forgotPasswordService.verifyCode(verifyCodeRequestDto.getEmail(), verifyCodeRequestDto.getCode())?
                        Map.of("verified", true) : Map.of("verified", false)
        );
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody ChangePasswordRequestDto dto) {
        forgotPasswordService.changePassword(dto);

        return ResponseEntity.ok(
                Map.of(
                        "changed", true,
                        "message", "Password updated successfully"
                )
        );
    }
}
