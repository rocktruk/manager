package com.online.mall.manager.control;

import java.math.BigDecimal;
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

import com.online.mall.manager.common.ConfigConstants;
import com.online.mall.manager.common.DictConstantsUtil;
import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.IdGenerater;
import com.online.mall.manager.common.ParamUtil;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.entity.Goods;
import com.online.mall.manager.entity.GoodsMenu;
import com.online.mall.manager.service.GoodsMenuService;
import com.online.mall.manager.service.GoodsService;

@Controller
public class GoodsManagerControl {

	private static final Logger log = LoggerFactory.getLogger(GoodsManagerControl.class);
	
	
	@Autowired
	private GoodsMenuService service;
	
	@Autowired
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
		List<GoodsMenu> menus = service.findAll();
		request.setAttribute("menus", menus);
		return "goodsmanager/goods";
	}
	
	
	@RequestMapping("/goodsLoad")
	@ResponseBody
	public Map<String,Object> loadGoodsWithPage(HttpServletRequest request,@RequestParam(value = "length") int length,
			@RequestParam(value = "start") int start){
		Map<String,Object> result = new HashMap<String, Object>();
		Sort sort = new Sort(Direction.DESC, "createTime");
		if(length == 0) {
			length = 10;
		}
		result = goodsService.searchGoodsForDataTable(null, sort, start, length) ;
		
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
		try {
			String respcode = ParamUtil.validatorColumn(req, new String[]{"menuName"});
			if(!respcode.equals(RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC))) {
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(respcode));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getMsgByCode(respcode));
				return result;
			}
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
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	@RequestMapping("addGoods")
	@ResponseBody
	public Map<String,Object> addGoods(@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			String code = ParamUtil.validatorColumn(req, new String[]{"brand","title","specification","price","inventory"});
			if(!code.equals(RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC))) {
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(code));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getMsgByCode(code));
				return result;
			}
			Goods goods = new Goods();
			String goodsId = IdGenerater.INSTANCE.goodsIdGenerate();
			goods.setBrand((String)req.get("brand"));
			goods.setCarriage(Integer.parseInt((String)req.get("carriage")));
			goods.setDesc((String)req.get("goodsDesc"));
			goods.setId(goodsId);
			goods.setInventory(Integer.parseInt((String)req.get("inventory")));
			GoodsMenu menu = new GoodsMenu();
			menu.setId(Integer.parseInt((String)req.get("menuId")));
			goods.setMenu(menu);
			goods.setOriPrice(req.get("oriPrice")==null?BigDecimal.ZERO.setScale(2):new BigDecimal((String)req.get("oriPrice")).setScale(2));
			goods.setPrice(new BigDecimal((String)req.get("price")).setScale(2));
			goods.setSpecification((String)req.get("specification"));
			goods.setStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_STATUS_TOSALE));
			goods.setTitle((String)req.get("title"));
			goodsService.saveGoods(goods);
			result.put("id", goodsId);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	/**
	 * 菜单图片上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String,Object> uploadImage(MultipartHttpServletRequest request)
	{
		
		log.debug("开始上传菜单图片");
		String id = request.getParameter("id");
		MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
		MultipartFile img = ((MultipartFile)map.get("uploadfile").get(0));
		Map<String,Object> result = service.writeUploadFile(img, id);
		return result;
	}
	
	
	/**
	 * 商品图片上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/goodsImgUp")
	@ResponseBody
	public Map<String,Object> uploadGoodsImage(MultipartHttpServletRequest request)
	{
		log.debug("开始上传商品图片");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
		MultipartFile img;
		if(ConfigConstants.GOODS_IMG.equals(type)) {
			img = ((MultipartFile)map.get("goodsimg").get(0));
		}else {
			img = ((MultipartFile)map.get("uploadfile").get(0));
		}
		Map<String,Object> result = goodsService.writeGoodsUploadFile(img, id, type);
		return result;
	}
	
}
