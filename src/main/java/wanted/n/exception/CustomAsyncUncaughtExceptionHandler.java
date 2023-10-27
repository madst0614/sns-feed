package wanted.n.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 비동기 작업에서 발생한 예외를 처리하는 {@link AsyncUncaughtExceptionHandler}
 * GlobalExceptionHandelr (@ControllerAdvice, @ExceptionHandler)는
 * 다른 스레드에서 발생한 예외를 캐치하지 못하므로 스레드 풀을 사용할 때 설정된 예외 처리기
 * (예: ThreadPoolTaskExecutor에 설정된 AsyncUncaughtExceptionHandler)를 사용하여 예외 처리
 */
@Slf4j
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        log.error("Async Exception - " + throwable.getMessage());
        log.error("Async Method - " + method.getName());
        Arrays.stream(params).forEach(param -> log.error("Parameter value - " + param));
    }
}