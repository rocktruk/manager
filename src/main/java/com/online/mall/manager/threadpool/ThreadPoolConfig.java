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
        // è®¾ç½®? ¸å¿ƒçº¿ç¨‹æ•°
        executor.setCorePoolSize(5);
        // è®¾ç½®??å¤§çº¿ç¨‹æ•°
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()*2);
        // è®¾ç½®??Ÿå?—å®¹???
        executor.setQueueCapacity(10000);
        // è®¾ç½®çº¿ç?‹æ´»è·ƒæ—¶?—´ï¼ˆç?’ï??
        executor.setKeepAliveSeconds(60);
        // è®¾ç½®é»˜è®¤çº¿ç?‹å?ç§°
        executor.setThreadNamePrefix("shoppv-");
        // è®¾ç½®??’ç?ç?–ç•¥
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // ç­‰å?…æ???‰ä»»?Š¡ç»“æ?Ÿå?å?å…³?—­çº¿ç?‹æ??
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
	}
}
