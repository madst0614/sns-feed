package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.User;
import wanted.n.dto.RefreshTokenDTO;
import wanted.n.dto.TokenIssuanceDTO;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.repository.UserRepository;

import static wanted.n.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshTokenDTO issueNewAccessToken(String refreshToken) {

        validateRefreshToken(refreshToken);

        User user =
                findUser(jwtTokenProvider.getAccountFromToken(refreshToken));

        String newAccessToken =
                jwtTokenProvider.generateAccessToken(TokenIssuanceDTO.from(user));

        return RefreshTokenDTO.builder()
                .accessToken(newAccessToken)
                .account(user.getAccount())
                .build();
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_CODE_EXPIRED);
        }
    }

    private User findUser(String userAccount) {
        return userRepository.findByAccount(userAccount)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
