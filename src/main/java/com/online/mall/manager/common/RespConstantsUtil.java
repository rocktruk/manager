package com.online.mall.manager.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RespConstantsUtil {

	
	private static final Logger log = LoggerFactory.getLogger(RespConstantsUtil.class);
	
	public static final RespConstantsUtil INSTANCE = new RespConstantsUtil();
	
	private Properties dictConstants;
	
	private Map<String,String> map = new HashMap<String, String>();
	
	private RespConstantsUtil()
	{
		this.dictConstants = loadDict();
	}
	
	/**
	 * 加载业务字典配置文件
	 * @return
	 */
	private  Properties loadDict()
	{
		Properties dict = new Properties();
		try {
			dict.load(new FileInputStream(new File(RespConstantsUtil.class.getResource("/respcode.properties").getFile())));
			for(Entry<Object, Object> entry : dict.entrySet())
			{
				if(entry.getKey().toString().startsWith(IConstants.RESP_CODE))
				{
					String suffix = entry.getKey().toString().substring(entry.getKey().toString().indexOf("."));
					map.put(entry.getValue().toString(), dict.getProperty(IConstants.RESP_MSG+suffix));
				}
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return dict;
	}

	
	public String getMsgByCode(String code)
	{
		return map.get(code);
	}
	
	public Properties getDictConstants() {
		return dictConstants;
	}

	/**
	 * 根据字典key获取值
	 * @param key
	 * @return
	 */
	public String getDictVal(String key)
	{
		return (String)dictConstants.get(key);
	}
	
}
