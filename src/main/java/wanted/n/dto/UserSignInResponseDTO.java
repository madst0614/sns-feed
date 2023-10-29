package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInResponseDTO {
    private Long id;
    private String account;
    private String email;
    private String accessToken;
    private String refreshToken;

    public static UserSignInResponseDTO from(UserDTO userDto) {
        return UserSignInResponseDTO.builder()
                .id(userDto.getId())
                .account(userDto.getAccount())
                .email(userDto.getEmail())
                .accessToken(userDto.getAccessToken())
                .refreshToken(userDto.getRefreshToken())
                .build();
    }
}
