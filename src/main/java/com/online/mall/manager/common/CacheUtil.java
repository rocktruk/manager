package com.online.mall.manager.common;

import javax.annotation.Resource;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {

	@Resource
	private ApplicationContext context;
	
	public Object getCacheContent(String cacheName,String key) {
		SimpleValueWrapper wrap = (SimpleValueWrapper)((CacheManager)context.getBean("caffeineCacheManager")).getCache(cacheName).get(key);
		if(wrap != null) {
			return wrap.get();
		}else {
			return null;
		}
		
	}
	
	public void setCacheContent(String cacheName,String key,Object value) {
		((CacheManager)context.getBean("caffeineCacheManager")).getCache(cacheName).put(key, value);
	}
	
	
	public Object putIfAbsent(String cacheName,String key,Object value) {
		synchronized (key) {
			Object c =getCacheContent(cacheName,key);
			if(c != null) {
				return c;
			}
			setCacheContent(cacheName, key, value);
			return null;
		}
	}
	
}
