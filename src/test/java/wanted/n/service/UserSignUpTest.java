package wanted.n.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.UserSignUpRequestDTO;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("사용자 회원가입 테스트")
class UserSignUpTest {

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
                userRepository, passwordEncoder,jwtTokenProvider
        );
    }

    @Test
    @DisplayName("성공")
    public void testUserSignUp_Success() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("daehanminkuk193!")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.empty());
        //when
        userService.registerUser(userSignUpRequestDTO);

        //then
        verify(userRepository, times(1))
                .save(argThat(user ->
                        user.getAccount().equals(userSignUpRequestDTO.getAccount()) &&
                                user.getEmail().equals(userSignUpRequestDTO.getEmail())
                ));
    }

    @Test
    @DisplayName("실패 - 같은문자 3번이상 반복")
    public void testUserSignUp_Fail_PW_Numeric_Only() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("cbaaanbok")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> userService.registerUser(userSignUpRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessage("동일한 문자가 3개 이상 연속되면 사용할 수 없습니다");
    }

    @Test
    @DisplayName("실패 - 영숫자특문(2개이상) 조합이 아닌 비밀번호")
    public void testUserSignUp_Fail_PW_Too_Short() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("ajhgfasc")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> userService.registerUser(userSignUpRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessage("숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.");
    }

    @Test
    @DisplayName("실패 - 연속 문자가 포함된 비밀번호")
    public void testUserSignUp_Fail_PW_Continual() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("abcbanbok!")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> userService.registerUser(userSignUpRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessage("연속된 문자가 3개 이상 포함되면 사용할 수 없습니다");
    }

    @Test
    @DisplayName("실패 - 통상적인 비밀번호")
    public void testUserSignUp_Fail_PW_Common() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("password12")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.empty());

        //when&then
        assertThatThrownBy(() -> userService.registerUser(userSignUpRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessage("통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다.");
    }

    @Test
    @DisplayName("실패 - 계정 중복")
    public void testUserSignUp_Fail_ACC_Duplicated() {
        //given
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .account("테스트계정")
                .email("email@email.email")
                .password("test1093353")
                .build();

        when(userRepository.findByAccount(userSignUpRequestDTO.getAccount()))
                .thenReturn(Optional.of(new User()));

        //when&then
        assertThatThrownBy(() -> userService.registerUser(userSignUpRequestDTO))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 가입된 계정입니다.");
    }
}