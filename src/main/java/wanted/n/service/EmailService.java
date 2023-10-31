package wanted.n.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wanted.n.enums.MailComponents;
import wanted.n.exception.CustomException;

import static wanted.n.exception.ErrorCode.EMAIL_SENDING_FAILED;

/**
 * 이메일 관련 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    /**
     * 이메일을 전송하는 메서드 (인증번호 / 비밀번호 초기화 시)
     *
     * @param to         수신자 이메일 주소
     * @param subject    이메일 제목
     * @param text       이메일 내용
     * @param formatArgs 서식 지정자 배열
     */
    @Async
    public void sendEmail(String to, MailComponents subject, MailComponents text, Object[] formatArgs) {

        SimpleMailMessage mail = new SimpleMailMessage();

        createMail(mail, to, subject, text, formatArgs);

        send(mail);

        log.info("email 전송 성공! 수신자 : " + to);
    }

    /**
     * SimpleMailMessage 객체를 세팅하는 메서드
     *
     * @param to         수신자 이메일 주소
     * @param subject    이메일 제목
     * @param text       이메일 내용
     * @param formatArgs 서식 지정자 배열
     */
    private void createMail(SimpleMailMessage mail,
                            String to,
                            MailComponents subject,
                            MailComponents text,
                            Object[] formatArgs) {
        mail.setTo(to);
        mail.setSubject(subject.getContent());

        String formattedText = String.format(text.getContent(), formatArgs);

        mail.setText(formattedText);
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
