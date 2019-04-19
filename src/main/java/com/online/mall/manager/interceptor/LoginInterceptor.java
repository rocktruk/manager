package com.online.mall.manager.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.online.mall.manager.common.SessionUtil;
import com.online.mall.manager.common.SignatureUtil;
import com.online.mall.manager.entity.MerAdmin;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long start = System.currentTimeMillis();
		String requestId = UUID.randomUUID().toString();
		MDC.put("requestId", requestId);
		request.setAttribute("startTime", start);
		if("/login".equals(request.getServletPath())) {
			return true;
		}
		MerAdmin user = (MerAdmin)SessionUtil.getAttribute(request.getSession(), SessionUtil.USER);
		if(user == null)
		{
			response.sendRedirect("login");
			return false;
		}
		
		return true;
	}
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
