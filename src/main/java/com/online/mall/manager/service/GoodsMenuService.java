package com.online.mall.manager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.entity.GoodsMenu;
import com.online.mall.manager.repository.GoodsMenuRepository;


@Service
public class GoodsMenuService {

	@Value(value="${file.uploadFolder}")
	private String uploadPath;
	
	@Autowired
	private GoodsMenuRepository goodsMenu;
	
	private static final Logger log = LoggerFactory.getLogger(GoodsMenuService.class);
	
	
	public List<GoodsMenu> findAll()
	{
		List<GoodsMenu> ls = goodsMenu.findAll();
		return ls;
	}
	
	
	public int findByMenuName(String menuName)
	{
		Optional<GoodsMenu> menu = goodsMenu.findGoodsMenuByMenuName(menuName);
		return menu.get().getId();
	}
	
	/**
	 * 检查菜单名称是否存在，已存在返回错误码，否则插入菜单
	 * @param menu
	 * @return
	 */
	@Transactional
	public String addMenu(GoodsMenu menu)
	{
		try {
			Optional<GoodsMenu> classified = goodsMenu.findGoodsMenuByMenuName(menu.getMenuName());
			if(classified.isPresent())
			{
				return RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_MENU_EXIST);
			}
			goodsMenu.addMenu(menu.getId(), menu.getParentId(), menu.getMenuName(), menu.getImageSrc());
			return RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC);
		}catch(Exception e)
		{
			log.error(e.getMessage(),e);
			return RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR);
		}
	}
	
	@Transactional
	public void updateMenuIcon(int id,String imgSrc)
	{
		goodsMenu.updateMenuWithId(id,imgSrc);
	}
	
	
	public Map<String,Object> menuTable()
	{
		Map<String,Object> result = getMenuParent(findAll());
		return result;
	}
	
	/**
	 * 将菜单上传的文件写入到以菜单ID为目录名的文件夹下，并更新菜单图片地址
	 * @param img
	 * @param id
	 * @return
	 */
	@Transactional
	public Map<String,Object> writeUploadFile(MultipartFile img,String id)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		InputStream input=null;
		OutputStream out=null;
		try {
			input = img.getInputStream();
			String src = id+File.separator+img.getOriginalFilename();
			File f = new File(uploadPath+src);
			File dir = new File(uploadPath+id);
			if(!dir.exists())
			{
				dir.mkdir();
			}
			if(f.createNewFile())
			{
				out = new FileOutputStream(f);
				FileUtil.copyStream(input, out);
			}
			//更新菜单图片
			updateMenuIcon(Integer.parseInt(id),src);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}finally {
			try {
				out.flush();
				input.close();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		return result;
	}
	
	/**
	 * 获取所有一级菜单的ID集合,级所有菜单
	 * @param ls
	 * @return
	 */
	public  Map<String,Object> getMenuParent(List<GoodsMenu> ls)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Map<String,Object>> menuMap = new HashMap<String, Map<String,Object>>();
		List<GoodsMenu> parentLs = new ArrayList<GoodsMenu>();
		for(GoodsMenu menu : ls)
		{
			Map<String,Object> map = new HashMap<String, Object>();
			if(menu.getParentId()==0)
			{
				parentLs.add(menu);
			}else {
				//设置所有存在父级菜单的父菜单名称
				if(menuMap.get(menu.getParentId())!=null)
				{
					map.put("parentName", menuMap.get(menu.getParentId()).get("menuame"));
				}else
				{
					for(GoodsMenu godsMenu : ls)
					{
						if(godsMenu.getId()==menu.getParentId())
						{
							map.put("parentName", godsMenu.getMenuName());
						}
					}
				}
			}
			map.put("id", menu.getId());
			map.put("menuame", menu.getMenuName());
			map.put("parentId", menu.getParentId());
			menuMap.put(String.valueOf(menu.getId()), map);
		}
		result.put("classified", menuMap);
		result.put("parentLs", parentLs);
		return result;
	}
	
	
	
	private List<GoodsMenu> eachMenu(List<GoodsMenu> ls,int id)
	{
		List<GoodsMenu> childMenu = new ArrayList<GoodsMenu>();
		for(GoodsMenu menu : ls)
		{
			if(id == menu.getParentId())
			{
				childMenu.add(menu);
			}
		}
		return childMenu;
	}
	
}
