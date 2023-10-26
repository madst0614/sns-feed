package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.dto.UserSignUpRequest;
import wanted.n.service.EmailService;
import wanted.n.service.UserService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static wanted.n.enums.MailComponents.VERIFICATION_MESSAGE;
import static wanted.n.enums.MailComponents.VERIFICATION_SUBJECT;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(tags = "User API", description = "사용자와 관련된 API")
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    @ApiOperation(value = "회원가입", notes = "사용자가 회원정보를 입력하여 회원가입을 진행합니다.")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {

        // 입력받은 정보로 회원가입 (회원상태 : 미인증)
        userService.registerUser(userSignUpRequest);

        // 회원가입 인원에게 인증메일 전송
        emailService.sendEmail(
                userSignUpRequest.getEmail(), VERIFICATION_SUBJECT, VERIFICATION_MESSAGE
        );

        return ResponseEntity.status(CREATED).build();
    }

}
