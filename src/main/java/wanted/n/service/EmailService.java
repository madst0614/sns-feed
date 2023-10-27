package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import wanted.n.enums.MailComponents;
import wanted.n.exception.CustomException;

import java.util.UUID;

import static wanted.n.enums.MailComponents.VERIFICATION_SUBJECT;
import static wanted.n.exception.ErrorCode.EMAIL_SENDING_FAILED;

/**
 * 이메일 관련 서비스
 */
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    /**
     * 이메일을 전송하는 메서드 (인증번호 / 비밀번호 초기화 시)
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 주제
     * @param text    이메일 내용
     */
    public void sendEmail(String to, MailComponents subject, MailComponents text) {

        SimpleMailMessage mail = new SimpleMailMessage();

        // 인증 메일 일 경우 인증 코드를 보내는 메일 생성
        if (subject.equals(VERIFICATION_SUBJECT)) {
            createMail(mail, to, subject, text, 6);
        }

        send(mail);
    }

    /**
     * 인증 코드/임시 비밀번호 를 생성하는 메서드
     *
     * @param length 생성할 인증 코드의 길이
     * @return 생성된 인증 코드 / 바밀번호
     */
    private String createUUID(int length) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, length);
    }

    /**
     * SimpleMailMessage 객체를 생성하는 메서드
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 주제
     * @param text    이메일 내용
     * @param length  생성할 인증 코드의 길이
     * @return 생성된 SimpleMailMessage 객체
     */
    private SimpleMailMessage createMail(SimpleMailMessage mail,
                                         String to,
                                         MailComponents subject,
                                         MailComponents text, int length) {
        mail.setTo(to);
        mail.setSubject(subject.getContent());
        mail.setText(String.format(text.getContent(), createUUID(length)));
        return mail;
    }

    /**
     * 이메일을 전송하는 메서드
     *
     * @param mail SimpleMailMessage 객체
     */
    private void send(SimpleMailMessage mail) {
        try {
            javaMailSender.send(mail);
        } catch (MailException e) {
            throw new CustomException(EMAIL_SENDING_FAILED);
        }
    }
}
