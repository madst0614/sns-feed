package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVerificationRequestDTO {

    @NotBlank(message = "계정을 입력해주세요.")
    private String account;

    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하로 입력해 주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Size(min = 6, max = 6, message = "인증번호는 6자리 입니다.")
    @NotBlank(message = "인증번호를 입력해주세요.")
    private String otp;

}
