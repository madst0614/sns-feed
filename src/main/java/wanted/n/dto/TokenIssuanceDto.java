package wanted.n.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.n.domain.User;
import wanted.n.enums.UserRole;

@Getter
@Builder
public class TokenIssuanceDto {
    private Long id;
    private String email;
    private UserRole userRole;

    public static TokenIssuanceDto from(User user) {
        return TokenIssuanceDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }
}
