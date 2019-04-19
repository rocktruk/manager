package com.online.mall.manager.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 便于后续通过中间件缓存做session共享
 * @author kaka_lijian
 *
 */
@Component
public class SessionUtil {

	public static final String USER = "user";
	
	@Resource
	private ApplicationContext context;
	
	public static void setAttribute(HttpSession session,String key,Object value)
	{
		session.setAttribute(key,value);
	}
	
	
	public static Object getAttribute(HttpSession session,String key)
	{
		return session.getAttribute(key);
	}
	
	
}
