package wanted.n.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.TokenIssuanceDTO;
import wanted.n.dto.UserDTO;
import wanted.n.dto.UserSignInRequestDTO;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static wanted.n.enums.UserStatus.*;

@DisplayName("사용자 로그인 테스트")
public class UserSignInTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    @DisplayName("성공")
    public void signInUser_WithValidUser_ReturnsUserDTO() {
        //given
        UserSignInRequestDTO signInRequest =
                UserSignInRequestDTO.builder()
                        .email("test@example.com")
                        .password("password")
                        .build();

        User user = User.builder()
                .userStatus(VERIFIED)
                .password(passwordEncoder.encode(signInRequest.getPassword()))
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(any(TokenIssuanceDTO.class))).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(signInRequest.getEmail())).thenReturn("refreshToken");
        when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(true);

        //when
        UserDTO userDTO = userService.signInUser(signInRequest);

        //then
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("실패 - 미인증 사용자")
    public void signInUser_Fail_UnverifiedUser() {
        //given
        UserSignInRequestDTO signInRequest =
                UserSignInRequestDTO.builder()
                        .email("test@example.com")
                        .password("password")
                        .build();

        User user = User.builder()
                .userStatus(UNVERIFIED)
                .password(passwordEncoder.encode(signInRequest.getPassword()))
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));

        //when&then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> userService.signInUser(signInRequest))
                .withMessage("인증되지 않은 사용자 입니다. 이메일 인증을 완료해주세요.");
    }

    @Test
    @DisplayName("실패 - 탈퇴한 회원")
    public void signInUser_Fail_DeletedUser() {
        //given

        UserSignInRequestDTO signInRequest =
                UserSignInRequestDTO.builder()
                        .email("test@example.com")
                        .password("password")
                        .build();

        User user = User.builder()
                .userStatus(DELETED)
                .password(passwordEncoder.encode(signInRequest.getPassword()))
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));

        //when&then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> userService.signInUser(signInRequest))
                .withMessage("탈퇴한 사용자입니다.");
    }

    @Test
    @DisplayName("실패 - 비밀번호 불일치")
    public void signInUser_Fail_Invalid_Password() {
        //given
        UserSignInRequestDTO signInRequest =
                UserSignInRequestDTO.builder()
                        .email("test@example.com")
                        .password("password")
                        .build();

        User user = User.builder()
                .userStatus(VERIFIED)
                .password(passwordEncoder.encode("randompassword"))
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())).thenReturn(false);

        //when&then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> userService.signInUser(signInRequest))
                .withMessage("비밀번호가 일치하지 않습니다.");
    }
}
