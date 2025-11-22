package bg.senpai_main;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.MemberRepository;
import bg.senpai_main.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class Runner implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(memberRepository.findByUsername("ADMIN").isEmpty()){
            Member member = Member
                    .builder()
                    .role(Role.ADMIN)
                    .username("ADMIN")
                    .active(true)
                    .email("valentinbliznakov223@gmail.com")
                    .password(passwordEncoder.encode("ADMIN123"))
                    .registeredOn(LocalDateTime.now())
                    .build();

            memberRepository.save(member);
        }
    }
}
