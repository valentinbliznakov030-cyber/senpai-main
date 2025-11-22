package bg.senpai_main.services;

import bg.senpai_main.dtos.forgotPasswordDtos.ChangePasswordRequestDto;
import bg.senpai_main.entities.ForgotPasswordToken;
import bg.senpai_main.entities.Member;

public interface ForgotPasswordService {
    ForgotPasswordToken sendResetCode(String email);
    boolean verifyCode(String email, String code);
    Member changePassword(ChangePasswordRequestDto changePasswordRequestDto);
}