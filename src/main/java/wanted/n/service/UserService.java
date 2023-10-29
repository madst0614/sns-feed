package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.*;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;
import wanted.n.utility.ValidationUtil;

import static wanted.n.enums.UserStatus.*;
import static wanted.n.exception.ErrorCode.*;

/**
 * 사용자 관련 서비스
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 회원가입을 처리하는 메서드
     *
     * @param userSignUpRequestDTO 사용자 회원가입 요청 데이터
     */
    @Transactional
    public void registerUser(UserSignUpRequestDTO userSignUpRequestDTO) {

        String password = userSignUpRequestDTO.getPassword();

        // 계정 중복 체크
        userRepository.findByAccount(userSignUpRequestDTO.getAccount())
                .ifPresent(user -> {
                    throw new CustomException(DUPLICATED_ACCOUNT);
                });

        // 비밀번호 유효성 체크
        validatePassword(password);

        // 비밀번호를 암호화하여 저장
        userSignUpRequestDTO.setPassword(passwordEncoder.encode(password));
        userRepository.save(User.from(userSignUpRequestDTO));
    }

    /**
     * 비밀번호의 유효성을 검사하는 메서드 (private)
     *
     * @param password 검사할 비밀번호
     */
    private void validatePassword(String password) {
        if (ValidationUtil.hasSameCharacters(password)) {
            throw new CustomException(INVALID_PASSWORD_SAME_CHARACTERS);
        }

        if (ValidationUtil.hasConsecutiveCharacters(password)) {
            throw new CustomException(INVALID_PASSWORD_CONSECUTIVE_CHARACTERS);
        }

        if (ValidationUtil.isUsualPassword(password)) {
            throw new CustomException(INVALID_PASSWORD_USUAL_PASSWORD);
        }

        if (ValidationUtil.containsTwoOrMoreCharacterTypes(password)) {
            throw new CustomException(INVALID_PASSWORD_AT_LEAST_2_TYPES);
        }
    }

    /**
     * 사용자를 확인하고, 이미 인증된 사용자인지 확인한 후,
     * 입력된 비밀번호와 사용자의 저장된 비밀번호를 비교하여
     * 사용자를 인증하고 상태를 'VERIFIED'로 설정
     *
     * @param verificationRequest 사용자 인증 요청 정보
     */
    @Transactional
    public void verifyUser(UserVerificationRequestDTO verificationRequest) {

        User user = userRepository.findByAccount(verificationRequest.getAccount())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getUserStatus().equals(VERIFIED)) {
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

        isPasswordMatch(verificationRequest.getPassword(), user.getPassword());

        user.setUserStatus(VERIFIED);
    }

    /**
     * 사용자의 이메일을 확인하고, 이미 인증된 사용자인지 여부를 확인합니다.
     *
     * @param userOtpReIssueRequestDTO 사용자 OTP 재 발급 요청 DTO
     */
    public void checkUser(UserOtpReIssueRequestDTO userOtpReIssueRequestDTO) {
        User user = userRepository.findByAccount(userOtpReIssueRequestDTO.getAccount())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getUserStatus().equals(VERIFIED)) {
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

        if (!user.getEmail().equals(userOtpReIssueRequestDTO.getEmail())) {
            throw new CustomException(EMAIL_NOT_MATCH);
        }
    }


    /**
     * 사용자 로그인 처리를 하는 메서드
     * 사용자의 이메일을 확인하고, 사용자의 상태를 확인하여
     * 인증되지 않은 상태 또는 삭제된 상태인 경우 예외를 던집니다.
     * 그렇지 않으면 비밀번호를 비교하고,
     * 액세스 토큰 및 리프레시 토큰을 생성하여 사용자 정보를 반환합니다.
     *
     * @param signInRequest 사용자 로그인 요청 정보
     * @return UserDTO 사용자 정보 및 토큰 정보
     */
    public UserDTO signInUser(UserSignInRequestDTO signInRequest) {
        User user = userRepository.findByAccount(signInRequest.getAccount())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getUserStatus().equals(UNVERIFIED)) {
            throw new CustomException(USER_NOT_VERIFIED);
        } else if (user.getUserStatus().equals(DELETED)) {
            throw new CustomException(USER_DELETED);
        }

        isPasswordMatch(signInRequest.getPassword(), user.getPassword());

        String accessToken = jwtTokenProvider.generateAccessToken(TokenIssuanceDTO.from(user));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getAccount());

        return UserDTO.from(user, accessToken, refreshToken);
    }

    /**
     * 비밀번호 일치 여부를 확인하는 메서드
     *
     * @param password        입력된 비밀번호
     * @param encodedPassword 저장된 비밀번호 (해싱된)
     */
    private void isPasswordMatch(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
    }

    /**
     * 사용자 로그인 처리를 하는 메서드
     * 사용자의 이메일을 확인하고, 사용자의 상태를 확인하여
     * 인증되지 않은 상태 또는 삭제된 상태인 경우 예외를 던집니다.
     * 그렇지 않으면 비밀번호를 비교하고,
     * 액세스 토큰 및 리프레시 토큰을 생성하여 사용자 정보를 반환합니다.
     *
     * @param signInRequest 사용자 로그인 요청 정보
     * @return UserDTO 사용자 정보 및 토큰 정보
     */
    public UserDTO signInUser(UserSignInRequestDTO signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getUserStatus().equals(UNVERIFIED)) {
            throw new CustomException(USER_NOT_VERIFIED);
        } else if (user.getUserStatus().equals(DELETED)) {
            throw new CustomException(USER_DELETED);
        }

        isPasswordMatch(signInRequest.getPassword(), user.getPassword());

        String accessToken = jwtTokenProvider.generateAccessToken(TokenIssuanceDTO.from(user));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getAccount());

        return UserDTO.from(user, accessToken, refreshToken);
    }

    /**
     * 비밀번호 일치 여부를 확인하는 메서드
     *
     * @param password        입력된 비밀번호
     * @param encodedPassword 저장된 비밀번호 (해싱된)
     */
    private void isPasswordMatch(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
    }
}
