package wanted.n.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.UserVerificationRequestDTO;
import wanted.n.enums.UserStatus;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("사용자 인증 테스트")
public class UserVerificationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userRepository, passwordEncoder, jwtTokenProvider
        );
    }

    @Test
    @DisplayName("성공")
    public void testVerifyUser_Success() {
        //given
        String email = "user@example.com";
        String password = "password123";
        String otp = "123456";

        UserVerificationRequestDTO verificationRequest =
                new UserVerificationRequestDTO(email, password, otp);

        User user = User.builder()
                .email(email)
                .userStatus(UserStatus.UNVERIFIED)
                .password(passwordEncoder.encode(password))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        //when
        userService.verifyUser(verificationRequest);

        //then
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.VERIFIED);
    }

    @Test
    @DisplayName("실패 - 사용자를 찾을 수 없음")
    public void testVerifyUser_Fail_User_Not_Found() {
        //given
        String email = "user@example.com";
        String password = "password123";
        String otp = "123456";

        UserVerificationRequestDTO verificationRequest =
                new UserVerificationRequestDTO(email, password, otp);

        //when&then
        assertThatThrownBy(() -> userService.verifyUser(verificationRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("실패 - 이미 인증된 사용자")
    public void testVerifyUser_Fail_Already_Verified_User() {
        //given
        String email = "user@example.com";
        String password = "password123";
        String otp = "123456";

        UserVerificationRequestDTO verificationRequest =
                new UserVerificationRequestDTO(email, password, otp);

        User user = User.builder()
                .email(email)
                .userStatus(UserStatus.VERIFIED)
                .password(passwordEncoder.encode(password))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //when&then
        assertThatThrownBy(() -> userService.verifyUser(verificationRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 인증된 사용자입니다.");
    }

    @Test
    @DisplayName("실패 - 비밀번호가 일치하지 않음")
    public void testVerifyUser_Password_Not_Match() {
        //given
        String email = "user@example.com";
        String password = "password123";
        String wrongPassword = "wrongPassword";
        String otp = "123456";

        UserVerificationRequestDTO verificationRequest =
                new UserVerificationRequestDTO(email, password, otp);

        User user = User.builder()
                .email(email)
                .userStatus(UserStatus.UNVERIFIED)
                .password(passwordEncoder.encode(password))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        //when&then
        assertThatThrownBy(() -> userService.verifyUser(verificationRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }
}
