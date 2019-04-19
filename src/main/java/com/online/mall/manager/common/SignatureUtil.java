package com.online.mall.manager.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SignatureUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);
	
	private static String signkey;
	
	public static final SignatureUtil INTANCE = new SignatureUtil(signkey);
	
	private String privateKey;
	
	private String publicKey;
	
	private RSAPrivateKey rsaPrivate;
	
	private SignatureUtil(@Value("${signkey}") String signkey)
	{
		this.signkey = signkey;
		try {
			Map<String,Object> keyMap =RsaEntrycpt.generateKeyPair();
			publicKey = RsaEntrycpt.getPublicKey(keyMap);
			privateKey = RsaEntrycpt.getPrivateKey(keyMap);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	public String encryptByPublicKey(String data) {
		try {
			return Base64.getEncoder().encodeToString(RsaEntrycpt.encrypt(data, RsaEntrycpt.getPublicRSAKey(publicKey)));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "";
	}
	
	public String dencryptByPrivateKey(String data) {
		try {
			return new String(RsaEntrycpt.decrypt(RsaEntrycpt.decryptBase64(data), RsaEntrycpt.getPrivateRSAKey(privateKey)));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "";
	}
	
	/**
	 * 签名验证
	 * @param map
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public boolean checkSign(Map<String, Object> map) throws NoSuchAlgorithmException 
	{
		String sign = (String)map.get("sign");
		map.remove("sign");
		String check = sign(map);
		if(!sign.equals(check))
		{
			log.error("checksum:"+check+"|sign:"+sign);
			return false;
		}
		return true;
	}

	/**
	 * 以md5方式签名
	 */
	public String sign(Map<String, Object> map) throws NoSuchAlgorithmException
	{
		StringBuilder s = new StringBuilder();
		s.append(sortJoin(map));
		s = s.append(signkey);
		log.info("sign string:"+s.toString());
		return md5(s.toString());
	}
	
	/**
	 * md5方式计算签名
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String md5(String str) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(str.getBytes());
		return CoderUtil.getHexString(md.digest());
	}
	
	
	/**
	 * 将所有参数根据key的ASCII码顺序以key=value&key=value形式拼接
	 * @param map
	 * @param keys
	 * @return
	 */
	public String sortJoin(Map<String, Object> map)
	{
		List<String> ls = new ArrayList<String>(map.keySet());
		Collections.sort(ls);
		StringBuilder result = new StringBuilder();
		for(int i=0;i<ls.size();i++)
		{
			if(i>0) {
				result.append("&");
			}
			result.append(ls.get(i)).append("=").append(map.get(ls.get(i)));
		}
		return result.toString();
	}


	public String getPrivateKey() {
		return privateKey;
	}


	public String getPublicKey() {
		return publicKey;
	}
	
	
	
}
