package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.dto.UserSignUpRequest;
import wanted.n.dto.UserVerificationRequest;
import wanted.n.service.EmailService;
import wanted.n.service.RedisService;
import wanted.n.service.UserService;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static wanted.n.enums.MailComponents.VERIFICATION_MESSAGE;
import static wanted.n.enums.MailComponents.VERIFICATION_SUBJECT;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(tags = "User API", description = "사용자와 관련된 API")
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisService redisService;

    @PostMapping("/sign-up")
    @ApiOperation(value = "회원가입", notes = "사용자가 회원정보를 입력하여 회원가입을 진행합니다.")
    public ResponseEntity<Void> registerUser(
            @Valid @RequestBody UserSignUpRequest userSignUpRequest) {

        // 입력받은 정보로 회원가입 (회원상태 : 미인증)
        userService.registerUser(userSignUpRequest);

        // 회원가입 인원에게 인증메일 전송
        CompletableFuture<String> codeCompletableFuture = emailService.sendEmail(
                userSignUpRequest.getEmail(), VERIFICATION_SUBJECT, VERIFICATION_MESSAGE
        );

        // 이메일(key) , 인증코드(value) 로 하여 redis 에 저장
        redisService.saveOtp(userSignUpRequest.getEmail(), codeCompletableFuture);

        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/verification")
    @ApiOperation(value = "회원가입 인증", notes = "회원가입 인증을 진행합니다.")
    public ResponseEntity<Void> verifyUser(
            @Valid @RequestBody UserVerificationRequest verificationRequest){

        // 이메일(key)로 검색된 OTP와 입력받은 OTP가 일치하는지 확인
        redisService.otpVerification(verificationRequest.getEmail(), verificationRequest.getOtp());

        // 비밀번호도 검증 하고 회원상태를 인증으로 변경
        userService.verifyUser(verificationRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
