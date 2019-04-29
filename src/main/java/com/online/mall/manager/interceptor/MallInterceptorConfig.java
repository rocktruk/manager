package com.online.mall.manager.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallInterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private LoginInterceptor accessIntcpt;
	
	@Value(value="${file.uploadFolder}")
	private String uploadPath;
	 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessIntcpt).addPathPatterns("/**").excludePathPatterns("/js/**","/Angular/**","/css/**","/font-awesome/**","/img/**","/login","/authToken",uploadPath,"/error","/refundNotify");
		
	}
	
}
