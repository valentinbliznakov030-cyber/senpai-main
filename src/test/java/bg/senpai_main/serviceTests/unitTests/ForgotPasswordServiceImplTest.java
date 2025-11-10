package bg.senpai_main.serviceTests.unitTests;

import bg.senpai_main.dtos.FavoriteAddRequestDto;
import bg.senpai_main.entities.Anime;
import bg.senpai_main.entities.Favorite;
import bg.senpai_main.entities.ForgotPasswordToken;
import bg.senpai_main.entities.Member;
import bg.senpai_main.exceptions.EntityAlreadyExistException;
import bg.senpai_main.repositories.FavoriteRepository;
import bg.senpai_main.repositories.ForgotPasswordTokenRepository;
import bg.senpai_main.services.AnimeService;
import bg.senpai_main.services.ForgotPasswordService;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.impl.FavoriteServiceImpl;
import bg.senpai_main.services.impl.ForgotPasswordImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceImplTest {
    @Mock
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private ForgotPasswordImpl forgotPasswordImpl;


}
