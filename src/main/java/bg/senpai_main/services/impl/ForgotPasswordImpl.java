package bg.senpai_main.services.impl;

import bg.senpai_main.entities.ForgotPasswordToken;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.ForgotPasswordTokenRepository;
import bg.senpai_main.services.ForgotPasswordService;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ForgotPasswordImpl implements ForgotPasswordService {
    private final ForgotPasswordTokenRepository tokenRepository;
    private final MemberService memberService;
    private final JavaMailSender mailSender;

    @Override
    public void sendResetCode(String email) {
        tokenRepository.deleteByEmail(email);
        memberService.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Member not found"));

        String code = generate6DigitCode();

        ForgotPasswordToken token = ForgotPasswordToken.builder()
                .email(email)
                .token(code)
                .expiration(LocalDateTime.now().plusMinutes(10))
                .build();

        tokenRepository.save(token);

        sendEmail(email, code);
    }
    //sifhsdifnsfkcnsdfns
    @Override
    public boolean verifyCode(String email, String code) {
        Optional<ForgotPasswordToken> tokenOpt = tokenRepository.findByEmail(email);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        ForgotPasswordToken token = tokenOpt.get();

        return token.getToken().equals(code) && !token.isExpired();
    }


    private String generate6DigitCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Senpai.bg – Код за нулиране на паролата");
        message.setText("""
                Здравей!

                Твоят код за нулиране на паролата е: %s

                Този код е валиден 10 минути.
                Ако не си заявявал промяна на паролата, просто игнорирай това съобщение.

                Поздрави,
                Екипът на Senpai.bg
                """.formatted(code));

        mailSender.send(message);
    }
}
