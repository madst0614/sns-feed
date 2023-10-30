package wanted.n.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.UserPasswordResetRequestDTO;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static wanted.n.enums.UserStatus.DELETED;
import static wanted.n.enums.UserStatus.VERIFIED;

@DisplayName("사용자 비밀번호 초기화 테스트")
class UserPasswordResetTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userRepository, passwordEncoder, jwtTokenProvider
        );
    }

    @Test
    @DisplayName("실패 - 없는 유저")
    public void resetPassword_UserNotFound_ThrowsException() {
        //given
        UserPasswordResetRequestDTO passwordResetRequest =
                new UserPasswordResetRequestDTO("테스트계정", "test@example.com");

        String temporaryPassword = "newTempPassword";

        when(userRepository.findByAccount(passwordResetRequest.getAccount())).thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> userService.resetPassword(passwordResetRequest, temporaryPassword))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
        verify(userRepository).findByAccount(passwordResetRequest.getAccount());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("실패 - 이메일 불일치")
    public void resetPassword_EmailNotMatch_ThrowsException() {
        //given
        UserPasswordResetRequestDTO passwordResetRequest =
                new UserPasswordResetRequestDTO("테스트계정", "test2@example.com");

        String temporaryPassword = "newTempPassword";

        User existingUser =
                User.builder()
                        .userStatus(VERIFIED)
                        .password(passwordEncoder.encode("Password"))
                        .email("test@example.com")
                        .account("테스트계정")
                        .build();

        when(userRepository.findByAccount(passwordResetRequest.getAccount())).thenReturn(Optional.of(existingUser));

        //when&then
        assertThatThrownBy(() -> userService.resetPassword(passwordResetRequest, temporaryPassword))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이메일이 일치하지 않습니다.");
        verify(userRepository).findByAccount(passwordResetRequest.getAccount());
    }

    @Test
    @DisplayName("성공")
    public void resetPassword_Success() {
        //given
        UserPasswordResetRequestDTO passwordResetRequest =
                new UserPasswordResetRequestDTO("테스트계정", "test@example.com");

        String temporaryPassword = "newTempPassword";

        User existingUser =
                User.builder()
                        .userStatus(VERIFIED)
                        .password(passwordEncoder.encode("Password"))
                        .email(passwordResetRequest.getEmail())
                        .account(passwordResetRequest.getAccount())
                        .build();

        when(userRepository.findByAccount(passwordResetRequest.getAccount()))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(temporaryPassword)).thenReturn("hashedPassword");

        //when
        userService.resetPassword(passwordResetRequest, temporaryPassword);

        //then
        Assertions.assertThat(existingUser.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    @DisplayName("실패 - 활성화되지 않은 유저")
    public void resetPassword_Inactive_User() {
        //given
        UserPasswordResetRequestDTO passwordResetRequest =
                new UserPasswordResetRequestDTO("테스트계정", "test@example.com");

        String temporaryPassword = "newTempPassword";

        User existingUser =
                User.builder()
                        .userStatus(DELETED)
                        .password(passwordEncoder.encode("Password"))
                        .email(passwordResetRequest.getEmail())
                        .account(passwordResetRequest.getAccount())
                        .build();

        when(userRepository.findByAccount(passwordResetRequest.getAccount()))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(temporaryPassword)).thenReturn("hashedPassword");

        //when&then
        assertThatThrownBy(() -> userService.resetPassword(passwordResetRequest, temporaryPassword))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("탈퇴한 사용자 이거나 인증되지 않은 사용자입니다.");
    }
}