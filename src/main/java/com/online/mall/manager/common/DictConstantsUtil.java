package com.online.mall.manager.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictConstantsUtil {

	
	private static final Logger log = LoggerFactory.getLogger(DictConstantsUtil.class);
	
	public static final DictConstantsUtil INSTANCE = new DictConstantsUtil();
	
	private Properties dictConstants;
	
	private DictConstantsUtil()
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
			dict.load(new FileInputStream(new File(DictConstantsUtil.class.getResource("/dict.properties").getFile())));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return dict;
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
