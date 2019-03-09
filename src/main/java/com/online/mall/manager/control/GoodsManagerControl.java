package com.online.mall.manager.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.manager.entity.GoodsMenu;
import com.online.mall.manager.service.GoodsMenuService;

@Controller
public class GoodsManagerControl {

	@Autowired
	private GoodsMenuService service;
	
	@RequestMapping("/index")
	public String index()
	{
		return "index";
	}
	
	@RequestMapping("/menu")
	public String tables(HttpServletRequest request)
	{
		Map<String,Object> result = service.menuTable();
		request.setAttribute("params", result);
		return "table_data_tables";
	}
	
	
	@RequestMapping("addMenu")
	@ResponseBody
	public Map<String,Object> addMenu(HttpServletRequest request,@RequestBody Map<String,Object> req)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		GoodsMenu menu = new GoodsMenu();
		menu.setMenuName(req.get("menuName").toString());
		menu.setParentId(Integer.parseInt(req.get("parent").toString()));
		service.addMenu(menu);
		request.setAttribute("id",service.findByMenuName(req.get("menuName").toString()));
		return result;
	}
}
