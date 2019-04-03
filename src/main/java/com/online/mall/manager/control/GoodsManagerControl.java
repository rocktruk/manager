package com.online.mall.manager.control;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.entity.GoodsMenu;
import com.online.mall.manager.entity.GoodsWithoutDetail;
import com.online.mall.manager.service.GoodsMenuService;
import com.online.mall.manager.service.GoodsService;

@Controller
public class GoodsManagerControl {

	private static final Logger log = LoggerFactory.getLogger(GoodsManagerControl.class);
	
	
	@Autowired
	private GoodsMenuService service;
	
	
	private GoodsService goodsService;
	
	@RequestMapping("/index")
	public String index()
	{
		return "index";
	}
	
	@RequestMapping("/menu")
	public String showMenu(HttpServletRequest request)
	{
		Map<String,Object> result = service.menuParentTable();
		request.setAttribute("params", result);
		return "goodsmanager/menu";
	}
	
	
	@RequestMapping("/goods")
	public String showGoods(HttpServletRequest request)
	{
		return "goodsmanager/goods";
	}
	
	
	
	public Map<String,Object> loadGoodsWithPage(HttpServletRequest request,@RequestParam(value = "length") int length,
			@RequestParam(value = "start") int start){
		Map<String,Object> result = new HashMap<String, Object>();
		Sort sort = new Sort(Direction.DESC, "createTime");
		goodsService.findAllGoodsWithMenuAndPage(null, sort, 0, 10);
		return result;
	}
	
	
	/**
	 * 分页查询菜单
	 * @param request
	 * @param length
	 * @param start
	 * @param draw
	 * @return
	 */
	@RequestMapping("/loadMenuWithPage")
	@ResponseBody
	public Map<String,Object> loadMenuWithPage(HttpServletRequest request,@RequestParam(value = "length") int length,
			@RequestParam(value = "start") int start)
	{
		Map<String,Object> result = service.getMenuWithPage(length, start);
		return result;
	}
	
	
	@RequestMapping("addMenu")
	@ResponseBody
	public Map<String,Object> addMenu(@RequestBody Map<String,Object> req)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		GoodsMenu menu = new GoodsMenu();
		menu.setMenuName(req.get("menuName").toString());
		menu.setParentId(Integer.parseInt(req.get("parent").toString()));
		//添加新菜单
		String code = service.addMenu(menu);
		result.put(IConstants.RESP_MSG, code);
		result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getMsgByCode(code));
		//返回插入成功菜单的ID,用于后续图片上传的路径
		if(RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC).equals(code))
		{
			result.put("id",service.findByMenuName(req.get("menuName").toString()));
		}
		return result;
	}
	
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String,Object> uploadImage(MultipartHttpServletRequest request)
	{
		
		log.debug("开始上传图片");
		String id = request.getParameter("id");
		MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
		MultipartFile img = ((MultipartFile)map.get("uploadfile").get(0));
		Map<String,Object> result = service.writeUploadFile(img, id);
		return result;
	}
	
}
