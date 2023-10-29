package wanted.n.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenDTO {
    private String accessToken;
    private String account;
}
