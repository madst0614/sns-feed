package wanted.n.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //GlobalException
    UNDEFINED_EXCEPTION(HttpStatus.BAD_REQUEST, "알 수 없는 오류입니다."),

    //EmailException
    EMAIL_SENDING_FAILED(HttpStatus.BAD_REQUEST, "이메일 전송에 실패했습니다."),

    //UserException
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATED_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 가입된 계정입니다."),
    INVALID_PASSWORD_AT_LEAST_2_TYPES(HttpStatus.BAD_REQUEST, "숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
    INVALID_PASSWORD_SAME_CHARACTERS(HttpStatus.BAD_REQUEST, "동일한 문자가 3개 이상 연속되면 사용할 수 없습니다"),
    INVALID_PASSWORD_CONSECUTIVE_CHARACTERS(HttpStatus.BAD_REQUEST, "연속된 문자가 3개 이상 포함되면 사용할 수 없습니다"),
    INVALID_PASSWORD_USUAL_PASSWORD(HttpStatus.BAD_REQUEST, "통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다."),
    ALREADY_VERIFIED_USER(HttpStatus.BAD_REQUEST, "이미 인증된 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다."),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "요청되지 않은 이메일이거나 인증기간이 만료된 인증번호입니다. 인증번호를 다시 요청해주세요.");

    // IOException
    JSON_EXCEPTION(HttpStatus.BAD_REQUEST, "Json 직렬화에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
