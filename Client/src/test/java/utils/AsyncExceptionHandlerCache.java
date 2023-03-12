package utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@TestComponent
public class AsyncExceptionHandlerCache {
    @Getter
    private final List<Throwable> throwableList = new ArrayList<>();

    @Before("org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler.handleUncaughtException(Throwable ex, Method method, Object... params)")
    public void cacheException(JoinPoint joinPoint) {
        try {
            var throwable = (Throwable) joinPoint.getArgs()[0];
            if (throwable != null) {
                throwableList.add(throwable);
            }
        } catch (Exception e) {
            log.error("FAILED POINTCUT EXECUTION");
        }
    }
}
