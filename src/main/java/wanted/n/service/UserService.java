package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.domain.User;
import wanted.n.dto.UserSignUpRequest;
import wanted.n.dto.UserVerificationRequest;
import wanted.n.exception.CustomException;
import wanted.n.repository.UserRepository;
import wanted.n.utility.ValidationUtil;

import static wanted.n.enums.UserStatus.VERIFIED;
import static wanted.n.exception.ErrorCode.*;

/**
 * 사용자 관련 서비스
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 회원가입을 처리하는 메서드
     *
     * @param userSignUpRequest 사용자 회원가입 요청 데이터
     */
    @Transactional
    public void registerUser(UserSignUpRequest userSignUpRequest) {

        String password = userSignUpRequest.getPassword();

        // 계정 중복 체크
        userRepository.findByAccount(userSignUpRequest.getAccount())
                .ifPresent(user -> {
                    throw new CustomException(DUPLICATED_ACCOUNT);
                });

        // 비밀번호 유효성 체크
        validatePassword(password);

        // 비밀번호를 암호화하여 저장
        userSignUpRequest.setPassword(passwordEncoder.encode(password));
        userRepository.save(User.from(userSignUpRequest));
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

    @Transactional
    public void verifyUser(UserVerificationRequest verificationRequest) {

        User user = userRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getUserStatus().equals(VERIFIED)) {
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

        if (!passwordEncoder.matches(verificationRequest.getPassword(), user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        user.setUserStatus(VERIFIED);

        userRepository.save(user);
    }

    public void checkUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if(user.getUserStatus().equals(VERIFIED)){
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

    }
}
