package com.openroom.OpenRoom_backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // This enables Spring's asynchronous method execution capability
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // The number of threads to keep in the pool, even if they are idle.
        executor.setCorePoolSize(5);
        // The maximum number of threads to allow in the pool.
        executor.setMaxPoolSize(10);
        // The number of tasks to allow in the queue before rejecting new tasks.
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}