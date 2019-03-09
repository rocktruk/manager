package com.online.mall.manager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.GoodsMenu;
import com.online.mall.manager.repository.GoodsMenuRepository;


@Service
public class GoodsMenuService {

	@Autowired
	private GoodsMenuRepository goodsMenu;
	
	
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
	
	
	@Transactional
	public boolean addMenu(GoodsMenu menu)
	{
		int n = goodsMenu.addMenu(menu.getId(), menu.getParentId(), menu.getMenuName(), menu.getImageSrc());
		if(n==1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	
	public Map<String,Object> menuTable()
	{
		Map<String,Object> result = getMenuParent(findAll());
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
		Map<String,Object> parentLs = new HashMap<String, Object>();
		for(GoodsMenu menu : ls)
		{
			Map<String,Object> map = new HashMap<String, Object>();
			if(menu.getParentId()==0)
			{
				parentLs.put("id",menu.getId());
				parentLs.put("menuName",menu.getMenuName());
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
