package bg.senpai_main.serviceTests.unitTests;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.MemberRepository;
import bg.senpai_main.services.impl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;



    @Test
    public void WhenSuccessfulRegister_ThenReturnMember(){
        MemberRegisterDTO memberRegisterDTO = MemberRegisterDTO
                .builder()
                .email("akaza@gmail.com")
                .username("akaza-dono")
                .password("akaza-123")
                .build();


        Member member = Member.builder()
                        .id(UUID.randomUUID())
                        .email(memberRegisterDTO.getEmail())
                        .username(memberRegisterDTO.getUsername())
                        .password(memberRegisterDTO.getPassword())
                        .active(true)
                        .registeredOn(LocalDateTime.now())
                        .role(Role.USER)
                        .build();

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member savedMember = memberService.registerMember(memberRegisterDTO);

        assertThat(member.getId().compareTo(savedMember.getId())).isEqualTo(0);

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void WhenTryingRegisterButPersonExistByEmail_ThrowIllegalArgumentException(){

        Member alreadyExistingmember = Member.builder()
                    .id(UUID.randomUUID())
                    .email("akaza@gmail.com")
                    .username("akaza-dono")
                    .password("akaza-123")
                    .active(true)
                    .registeredOn(LocalDateTime.now())
                    .role(Role.USER)
                    .build();

        MemberRegisterDTO anotherPerson = MemberRegisterDTO
                    .builder()
                    .email("akaza@gmail.com")
                    .username("akaza-upm3")
                    .password("akaza_123")
                    .build();

        when(memberRepository.findByEmail(anotherPerson.getEmail())).thenReturn(Optional.of(alreadyExistingmember));

        assertThatThrownBy(() -> {
            memberService.registerMember(anotherPerson);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists!");

        verify(memberRepository).findByEmail(anotherPerson.getEmail());
    }

    @Test
    public void WhenTryingRegisterButPersonExistByUsername_ThrowIllegalArgumentException(){

        Member alreadyExistingmember = Member.builder()
                .id(UUID.randomUUID())
                .email("akaza@gmail.com")
                .username("akaza-dono")
                .password("akaza-123")
                .active(true)
                .registeredOn(LocalDateTime.now())
                .role(Role.USER)
                .build();

        MemberRegisterDTO anotherPerson = MemberRegisterDTO
                .builder()
                .email("douma@gmail.com")
                .username("akaza-dono")
                .password("douma_123")
                .build();

        when(memberRepository.findByUsername(anotherPerson.getUsername())).thenReturn(Optional.of(alreadyExistingmember));

        assertThatThrownBy(() -> {
            memberService.registerMember(anotherPerson);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already exists!");

        verify(memberRepository).findByUsername(anotherPerson.getUsername());

    }
}
