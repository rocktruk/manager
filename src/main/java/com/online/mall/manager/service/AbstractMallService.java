package com.online.mall.manager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class AbstractMallService {

	@Value(value="${file.uploadFolder}")
	protected String uploadPath;
	
	/**
	 * 将上传图片写入上传目录
	 * @param img
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public String writeFile(MultipartFile img,String id) throws IOException {
		InputStream input=null;
		OutputStream out=null;
		try {
			input = img.getInputStream();
			String src = id+File.separator+img.getOriginalFilename();
			File f = new File(uploadPath+src);
			File dir = new File(uploadPath+id);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			if(f.createNewFile())
			{
				out = new FileOutputStream(f);
				FileUtil.copyStream(input, out);
			}
			return src;
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}finally {
			try {
				out.flush();
				input.close();
				out.close();
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
	}
}
