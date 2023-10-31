package wanted.n.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.UserPasswordModifyRequestDTO;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static wanted.n.enums.UserStatus.VERIFIED;

@DisplayName("사용자 비밀번호 변경 테스트")
class UserPasswordModificationTest {

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
    @DisplayName("성공")
    public void modifyPassword_Success() {
        //given
        Long userId = 1L;
        String password = "existingPassword";
        String newPassword = "newPassword1!";

        UserPasswordModifyRequestDTO passwordModifyRequestDTO =
                new UserPasswordModifyRequestDTO(password, newPassword);

        User user = User.builder()
                .id(1L)
                .password(password)
                .userStatus(VERIFIED)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        //when
        userService.modifyPassword(userId, passwordModifyRequestDTO);

        //then
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("실패 - 기존비밀번호 불일치")
    public void modifyPassword_Existing_PW_Not_Match() {
        //given
        Long userId = 1L;
        String password = "existingPassword";
        String newPassword = "newPassword1!";

        UserPasswordModifyRequestDTO passwordModifyRequestDTO =
                new UserPasswordModifyRequestDTO(password, newPassword);

        User user = User.builder()
                .id(1L)
                .password("differentPassword")
                .userStatus(VERIFIED)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        //when&then
        assertThatThrownBy(() -> userService.modifyPassword(userId, passwordModifyRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }
}