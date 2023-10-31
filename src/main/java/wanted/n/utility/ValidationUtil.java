package wanted.n.utility;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * 비밀번호 유효성 검사를 위한 유틸 클래스 (총 4가지 조건으로 검사)
 */
@UtilityClass
public class ValidationUtil {

    /* 구글기준 2023 자주사용되는 비밀번호 30개 */
    private final List<String> USUAL_PASSWORD_LIST =
            Arrays.asList(
                    "1234", "27653", "12345", "qwerty", "dragon", "111111", "123321", "123456",
                    "654321", "abc123", "000000", "monkey", "1234567", "letmein", "12345678",
                    "password", "iloveyou", "sunshine", "zaq12wsx", "1qaz2wsx", "password12",
                    "superman", "princess", "1q2w3e4r", "123456789", "qwerty123", "asdfghjkl",
                    "1234567890", "qwertyuiop"
            );

    /* 비밀번호 조건: 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.*/
    public static boolean containsTwoOrMoreCharacterTypes(String password) {
        int characterTypeCount = 0;

        //문자가 포함되어 있는지
        if (password.matches(".*[a-zA-Z].*")) {
            characterTypeCount++;
        }

        //숫자가 포함되어 있는지
        if (password.matches(".*[0-9].*")) {
            characterTypeCount++;
        }

        //특수문자가 포함되어 있는지
        if (password.matches(".*[!@#$%^&*()].*")) {
            characterTypeCount++;
        }

        //문자, 숫자, 특수문자 중 2가지 이상을 포함하고 있으면 false, 아니면 true
        return characterTypeCount < 2;
    }

    /* 비밀번호 조건: 같은 문자가 3개 이상 포함되면 사용할 수 없습니다.*/
    public static boolean hasSameCharacters(String password) {
        return IntStream.range(0, password.length() - 3 + 1)
                .anyMatch(i -> password.substring(i, i + 3)
                        .chars()
                        .allMatch(c -> c == password.charAt(i)));
    }

    /* 비밀번호 조건: 연속된 문자 및 숫자가 3개 이상 포함되면 사용할 수 없습니다.*/
    public static boolean hasConsecutiveCharacters(String password) {
        return IntStream.range(0, password.length() - 2)
                .anyMatch(i -> {
                    char currentChar = password.charAt(i);
                    char nextChar = password.charAt(i + 1);
                    char secondNextChar = password.charAt(i + 2);
                    return isSequential(currentChar, nextChar, secondNextChar);
                });
    }

    /* 연속된 문자/숫자인지 판별 */
    private boolean isSequential(char a, char b, char c) {
        if ((Character.isLetter(a) && Character.isLetter(b) && Character.isLetter(c)) ||
                (Character.isDigit(a) && Character.isDigit(b) && Character.isDigit(c))) {
            int diff1 = b - a;
            int diff2 = c - b;
            if (diff1 == 1 && diff2 == 1) {
                return true;
            }
        }
        return false;
    }

    /* 비밀번호 조건: 통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다. */
    public static boolean isUsualPassword(String password) {
        return USUAL_PASSWORD_LIST.contains(password);
    }

    /**
     * 인증 코드/임시 비밀번호 를 생성하는 메서드
     *
     * @param length 생성할 인증 코드의 길이
     * @return 생성된 인증 코드 / 바밀번호
     */
    public static String createUUID(int length) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, length);
    }
}

