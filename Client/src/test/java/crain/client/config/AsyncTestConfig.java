package crain.client.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@EnableAsync
@TestConfiguration
public class AsyncTestConfig implements AsyncConfigurer {
    private ThrowableCachingAsyncConfig asyncUncaughtExceptionHandler;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ThrowableCachingAsyncConfig throwableCachingAsyncConfig() {
        this.asyncUncaughtExceptionHandler = new ThrowableCachingAsyncConfig();
        return this.asyncUncaughtExceptionHandler;
    }

    /**
     * The {@link Executor} instance to be used when processing async
     * method invocations.
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return this.asyncUncaughtExceptionHandler;
    }

    static class ThrowableCachingAsyncConfig implements AsyncUncaughtExceptionHandler {
        private final List<Throwable> throwable = new ArrayList<>();

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            this.throwable.add(ex);
        }

        public List<Throwable> getThrowables() {
            return throwable;
        }
    }
}
