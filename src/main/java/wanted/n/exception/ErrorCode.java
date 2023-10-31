package wanted.n.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //GlobalException
    UNDEFINED_EXCEPTION(HttpStatus.BAD_REQUEST, "알 수 없는 오류입니다."),

    //PostingException
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "Posting이 존재하지 않습니다."),
    INVALID_PAGINATION_OFFSET(HttpStatus.BAD_REQUEST, "page offset에 음수가 들어갈 수 없습니다."),
    INVALID_PAGINATION_SIZE(HttpStatus.BAD_REQUEST, "page size에 음수가 들어갈 수 없습니다."),

    //StatisticsException
    Statistics_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 통계가 존재하지 않습니다."),

    //DateException
    INVALID_PERIOD(HttpStatus.BAD_REQUEST, "유효하지 않은 기간입니다."),
    INVALID_DURATIONWITHDAY(HttpStatus.BAD_REQUEST, "최대 한달(30일) 조회 가능합니다."),
    INVALID_DURATIONWITHHOUR(HttpStatus.BAD_REQUEST, "최대 일주일(7일) 조회 가능합니다."),

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
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "요청되지 않은 이메일이거나 인증기간이 만료된 인증번호입니다. 인증번호를 다시 요청해주세요."),
    USER_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "인증되지 않은 사용자 입니다. 이메일 인증을 완료해주세요."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),
    EMAIL_NOT_MATCH(HttpStatus.BAD_REQUEST, "이메일이 일치하지 않습니다."),

    // IOException
    JSON_EXCEPTION(HttpStatus.BAD_REQUEST, "Json 직렬화에 실패했습니다."),

    // Hashtag
    HOT_HASHTAG_OK(HttpStatus.OK, "최근 3시간동안 많이 사용된 hot hashtag 입니다."),
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "HashTag가 존재하지 않습니다."),

    // 토큰 관련 Exception
    REFRESH_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다.");


    private final HttpStatus status;
    private final String message;
}
