package com.online.mall.manager.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

	@Bean
	public TaskExecutor createTaskExecute()
	{
	
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置?��心线程数
        executor.setCorePoolSize(5);
        // 设置??大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()*2);
        // 设置??��?�容???
        executor.setQueueCapacity(10000);
        // 设置线�?�活跃时?��（�?��??
        executor.setKeepAliveSeconds(60);
        // 设置默认线�?��?�称
        executor.setThreadNamePrefix("shoppv-");
        // 设置??��?��?�略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等�?��???�任?��结�?��?��?�关?��线�?��??
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
	}
}
