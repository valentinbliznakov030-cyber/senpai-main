package bg.senpai_main.serviceTests;

import bg.senpai_main.repositories.ForgotPasswordTokenRepository;
import bg.senpai_main.services.MemberService;
import bg.senpai_main.services.impl.ForgotPasswordImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceImplTest {
    @Mock
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private ForgotPasswordImpl forgotPasswordImpl;


}
