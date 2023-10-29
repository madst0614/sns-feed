package wanted.n.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.n.domain.User;

@Getter
@Builder
public class UserDTO {
    private Long id;
    private String account;
    private String email;
    private String accessToken;
    private String refreshToken;

    public static UserDTO from(User user, String accessToken, String refreshToken) {
        return UserDTO.builder()
                .id(user.getId())
                .account(user.getAccount())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
