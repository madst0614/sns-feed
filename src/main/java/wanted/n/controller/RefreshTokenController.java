package wanted.n.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.dto.RefreshTokenDTO;
import wanted.n.dto.RefreshTokenRequestDTO;
import wanted.n.dto.RefreshTokenResponseDTO;
import wanted.n.service.RefreshTokenService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Api(tags = "Auth API", description = "인증과 관련된 API")
@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

        String refreshToken = refreshTokenRequestDTO.getRefreshToken();

        // 토큰 재발급 서비스 호출
        RefreshTokenDTO refreshTokenDTO =
                refreshTokenService.issueNewAccessToken(refreshToken);

        return ResponseEntity.status(OK).body(RefreshTokenResponseDTO.from(refreshTokenDTO));
    }
}