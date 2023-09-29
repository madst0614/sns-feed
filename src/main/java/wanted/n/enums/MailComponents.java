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
                    "코드 : %s"
    ),

    NOTIFICATION_SUBJECT("단기간 조회수 급상승 메일입니다."),

    NOTIFICATION_MESSAGE(
            "SNS-FEED 귀하의 글이 단기간 내에 높은 조회수를 기록하고 있습니다! \n" +
                    "글 제목: [%s] \n " +
                    "조회수: [%s] \n" +
                    "사용자들의 관심을 끈 글을 작성해주셔서 감사합니다. "
    );

    private final String content;
}
