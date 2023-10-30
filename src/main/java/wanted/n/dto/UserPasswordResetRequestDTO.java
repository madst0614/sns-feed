package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPasswordResetRequestDTO {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "sns 계정을 입력해주세요.")
    private String account;
}
