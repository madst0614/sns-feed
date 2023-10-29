package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenResponseDTO {
    private String accessToken;

    public static RefreshTokenResponseDTO from(RefreshTokenDTO refreshTokenDTO) {
        return RefreshTokenResponseDTO.builder()
                .accessToken(refreshTokenDTO.getAccessToken())
                .build();
    }
}
