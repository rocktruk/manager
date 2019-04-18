package com.online.mall.manager.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import com.online.mall.manager.entity.GoodsWithoutDetail;
import com.online.mall.manager.entity.RecommendCommodities;
import com.online.mall.manager.service.GoodsMenuService;
import com.online.mall.manager.service.GoodsService;
import com.online.mall.manager.service.RcmndCommdyService;

@Controller
public class GoodsManagerControl {

	private static final Logger log = LoggerFactory.getLogger(GoodsManagerControl.class);
	
	
	@Autowired
	private GoodsMenuService service;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private RcmndCommdyService remndService;
	
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
	public Map<String,Object> loadGoodsWithPage(HttpServletRequest request,@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		Sort sort;
		int orderColumn = req.get("orderColumn") == null?0:(Integer)req.get("orderColumn");
		int start = (Integer)req.get("start");
		int length = (Integer)req.get("length");
		String orderSort = (String)req.get("orderSort");
		String search = (String)req.get("search");
		if (orderColumn < 1) {
			sort = new Sort(Direction.DESC, "createTime");
		}else {
			sort = new Sort("asc".equals(orderSort)?Direction.ASC:Direction.DESC, goodsService.orderGoods(orderColumn));
		}
		if(length == 0) {
			length = 10;
		}
		if(search == null || "".equals(search)) {	
			result = goodsService.searchGoodsForDataTable(null, sort, start, length) ;
		}else {
			GoodsWithoutDetail goods = new GoodsWithoutDetail();
			goods.setTitle(search);
			result = goodsService.searchGoodsForDataTable(goods, sort, start, length) ;
		}
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
	
	/**
	 * 商品新增
	 * @param req
	 * @return
	 */
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
	 * 更新商品
	 * @param req
	 * @return
	 */
	@RequestMapping("updGoods")
	@ResponseBody
	public Map<String,Object> updGoods(@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			String code = ParamUtil.validatorColumn(req, new String[]{"goodsId","brand","title","specification","price","inventory"});
			if(!code.equals(RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC))) {
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(code));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getMsgByCode(code));
				return result;
			}
			String goodsId = (String)req.get("goodsId");
			Goods goods = new Goods();
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
	 * 删除商品,不允许删除商品
	 * @param ids
	 * @return
	 */
//	@RequestMapping("delGoods")
//	@ResponseBody
	public Map<String,Object> delGoods(@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			goodsService.deleteGoods((List<String>)req.get("ids"));
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
	 *更新商品状态，上下架操作
	 * @param req
	 * @return
	 */
	@RequestMapping("goodsStatusUpd")
	@ResponseBody
	public Map<String,Object> updGoodsStatus(@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			goodsService.updateGoodsStatus(req.get("goodsId"), req.get("statu"));
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
	 *批量更新商品状态，上下架操作
	 * @param req
	 * @return
	 */
	@RequestMapping("batchUpdStatus")
	@ResponseBody
	public Map<String,Object> updBatchGoodsStatus(@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			goodsService.updateBatchGoodsStatus(req.get("goodsId"), req.get("statu"));
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
		List<MultipartFile> img;
		if(ConfigConstants.GOODS_IMG.equals(type)) {
			img = map.get("goodsimg");
		}else if (ConfigConstants.GOODS_BANNER_IMG.equals(type)){
			img = map.get("uploadfile");
		}else {
			img = map.get(request.getParameter("file").toString());
		}
		Map<String,Object> result = new HashMap<String, Object>();
		if(ConfigConstants.GOODS_RCMND_IMG.equals(type)) {
			result = remndService.writeRcmndUploadFile(img, id, type);
		}else {
			result = goodsService.writeGoodsUploadFile(img, id, type);
		}
		return result;
	}
	
	
	@RequestMapping("rcmndCommdy")
	@ResponseBody
	public Map<String,Object> setRcmndCommody(@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			ParamUtil.validatorColumn(req, new String[] {"rcmndType"});
			String rcmndType = (String)req.get("rcmndType");
			List<RecommendCommodities> rcmmdCommdities = new ArrayList<RecommendCommodities>();
			if("1".equals(rcmndType)) {
				List<String> ls = (List<String>)req.get("hotGoods");
				//获取所有热销商品的ID及关联ID
				List<Object[]> olds = remndService.getAllHotRcmndCommdities();
				List<String> rcmnds = new ArrayList<String>(olds.size());
				List<String> goodsIds = new ArrayList<String>(olds.size());
				//分组关联表ID,及商品ID
				for(Iterator<Object[]> it = olds.iterator();it.hasNext();) {
					Object[] rcmnd = it.next();
					rcmnds.add(rcmnd[0].toString());
					goodsIds.add(rcmnd[1].toString());
				}
				for(String s : ls) {
					//关联表已存在该商品，直接取该商品的关联ID，用于返回前台上传图片
					int a = goodsIds.indexOf(s);
					if(a > -1) {
						result.put(s, rcmnds.get(a));
						continue;
					}
					RecommendCommodities rcmndCommdy = new RecommendCommodities();
					rcmndCommdy.setId(IdGenerater.INSTANCE.rcmndIdGenerate());
					GoodsWithoutDetail goods = new GoodsWithoutDetail();
					goods.setId(s);
					rcmndCommdy.setGoods(goods);
					rcmndCommdy.setRcmndType(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RCMND_BANNER));
					rcmmdCommdities.add(rcmndCommdy);
					result.put(s, rcmndCommdy.getId());
				}
				remndService.addRcmnd(rcmmdCommdities, goodsIds,ls);
			}else {
				List<String> ls = (List<String>)req.get("rcmndGoods");
				for(String s : ls) {
					RecommendCommodities rcmndCommdy = new RecommendCommodities();
					rcmndCommdy.setId(IdGenerater.INSTANCE.rcmndIdGenerate());
					GoodsWithoutDetail goods = new GoodsWithoutDetail();
					goods.setId(s);
					rcmndCommdy.setGoods(goods);
					rcmndCommdy.setRcmndType(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RCMEN_GOODSLS));
					rcmmdCommdities.add(rcmndCommdy);
				}
				remndService.addRcmnd(rcmmdCommdities, remndService.getRcmndCommdy(),ls);
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
	
	
	/**
	 * 加载推荐商品selector
	 * @return
	 */
	@RequestMapping("loadGoodsSelector")
	@ResponseBody
	public Map<String,Object> loadGoodsSelect(){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			//获取所有已上架商品
			List<GoodsWithoutDetail> goods = goodsService.findAllGoodsPutAway();
			//获取首页轮播商品
			List<String> hotSales = remndService.hotSale();
			//获取首页推荐商品
			List<String> rcmndCommdies = remndService.getRcmndCommdy();
			result.put("rcmndLs", goods);
			result.put("hotsales", hotSales);
			result.put("rcmndGoods", rcmndCommdies);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
}
