package top.wteng.mqttbootintegration.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfiguration implements AsyncConfigurer {
    public final int CORE_POOL_SIZE = 5;
    public final int MAX_POOL_SIZE = 10;
    public final int QUEUE_CAPACITY = 50;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("MQTT-WORKER - ");
        executor.setKeepAliveSeconds(120);
        executor.initialize();
        return executor;
    }
}
