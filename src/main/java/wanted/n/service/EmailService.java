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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static wanted.n.enums.MailComponents.NOTIFICATION_MESSAGE;
import static wanted.n.enums.MailComponents.VERIFICATION_SUBJECT;
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
     * 이메일을 전송하는 메서드 (인증번호 / 비밀번호 초기화 시)
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 제목
     * @param text    이메일 내용
     */
    @Async
    public CompletableFuture<String> sendEmail(String to,
                                               MailComponents subject,
                                               MailComponents text) {

        SimpleMailMessage mail = new SimpleMailMessage();
        String randomString = null;

        // 인증 메일 일 경우 인증 코드를 보내는 메일 생성
        if (subject.equals(VERIFICATION_SUBJECT)) {
            randomString = createUUID(6);
            createMail(mail, to, subject, text, randomString);
        }

        send(mail);
        log.info("email 전송 성공! 수신자 : " + to);

        return CompletableFuture.completedFuture(randomString);
    }

    /**
     * SimpleMailMessage 객체를 세팅하는 메서드
     *
     * @param to               수신자 이메일 주소
     * @param subject          이메일 제목
     * @param text             이메일 내용
     * @param verificationCode 생성된 인증코드
     */
    private void createMail(SimpleMailMessage mail,
                            String to,
                            MailComponents subject,
                            MailComponents text,
                            String verificationCode) {
        mail.setTo(to);
        mail.setSubject(subject.getContent());
        mail.setText(text.getContent() + verificationCode);
    }

    /**
     * 이메일을 전송하는 메서드 (조회수 급상승)
     *
     * @param to      수신자 이메일 주소
     * @param subject 이메일 주제
     * @param text    이메일 내용 (title, view)
     */
    public void sendOnFireEmail(String to, MailComponents subject, MailComponents text, String title, long view) {

        SimpleMailMessage mail = new SimpleMailMessage();

        // 단기간 조회수 급상승 메일인 경우, 알림 메일 생성
        if (subject.equals(NOTIFICATION_MESSAGE)) {
            createOnFireMail(mail, to, subject, text, title, view);
        }

        send(mail);
    }

    /**
     * 단기간 조회수 급상승 메일을 생성하는 메서드
     *
     * @param mail 객체
     * @param to 수신자
     * @param subject 이메일 주제
     * @param text 이메일 내용
     * @param title 글 제목
     * @param view 조회수
     * @return 수정된 결과
     */
    private void createOnFireMail(SimpleMailMessage mail,
                                               String to,
                                               MailComponents subject,
                                               MailComponents text, String title, long view) {
        mail.setTo(to);
        mail.setSubject(subject.getContent());
        String mailText = String.format(text.getContent(), title, view);
        mail.setText(mailText);
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
