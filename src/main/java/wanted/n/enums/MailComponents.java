package wanted.n.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailComponents {
    VERIFICATION_SUBJECT("회원가입 인증 메일입니다."),

    VERIFICATION_MESSAGE(
            "SNS-FEED 회원가입을 축하합니다! \n" +
                    "아래 코드를 입력하여 회원인증을 완료하신 후 \n " +
                    "서비스를 사용하실 수 있습니다. \n" +
                    "코드 : "
    );

    private final String content;
}
