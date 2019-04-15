package com.online.mall.manager.interceptor;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.online.mall.manager.entity.MerAdmin;
import com.online.mall.manager.service.MerAdminService;
import com.online.mall.manager.token.LoginToken;

public class AuthenticationInterceptor implements HandlerInterceptor {

	@Autowired
	private MerAdminService merService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("Authorization");// 从 http 请求头中取出 token
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		// 检查有没有需要用户权限的注解
		if (method.isAnnotationPresent(LoginToken.class)) {
			LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
			if (userLoginToken.required()) {
				// 执行认证
				if (token == null) {
					throw new RuntimeException("无token，请重新登录");
				}
				// 获取 token 中的 user id
				String userId;
				try {
					userId = JWT.decode(token).getAudience().get(0);
				} catch (JWTDecodeException j) {
					throw new RuntimeException("401");
				}
				Optional<MerAdmin> user = merService.findMerById(Integer.parseInt(userId));
				if (!user.isPresent()) {
					throw new RuntimeException("用户不存在，请重新登录");
				}
				// 验证 token
				JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.get().getPwd())).build();
				try {
					jwtVerifier.verify(token);
				} catch (JWTVerificationException e) {
					throw new RuntimeException("401");
				}
				return true;
			}
		}
		return true;
	}
}
