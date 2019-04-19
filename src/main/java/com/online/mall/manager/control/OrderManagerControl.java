package com.online.mall.manager.control;

import java.security.NoSuchAlgorithmException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.ParamUtil;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.common.SignatureUtil;
import com.online.mall.manager.service.ShoppingOrderService;

@Controller
public class OrderManagerControl {

	private static final Logger log = LoggerFactory.getLogger(OrderManagerControl.class);
	
	@Autowired
	private ShoppingOrderService orderSrv;
	
	@RequestMapping("order")
	public String order(HttpServletRequest request) {
		
		return "goodsmanager/order";
	}
	
	
	@RequestMapping("loadOrders")
	@ResponseBody
	public Map<String,Object> loadOrders(@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Sort sort;
			int orderColumn = req.get("orderColumn") == null?0:(Integer)req.get("orderColumn");
			int start = (Integer)req.get("start");
			int length = (Integer)req.get("length");
			String orderSort = (String)req.get("orderSort");
			String search = (String)req.get("search");
			if (orderColumn < 1) {
				sort = new Sort(Direction.DESC, "createTime");
			}else {
				sort = new Sort("asc".equals(orderSort)?Direction.ASC:Direction.DESC, orderSrv.orderGoods(orderColumn));
			}
			if(length == 0) {
				length = 10;
			}
			result = orderSrv.searchOrder(sort, start, length);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	@RequestMapping("refund")
	@ResponseBody
	public Map<String,Object> refund(HttpServletRequest request,@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		String respcode = ParamUtil.validatorColumn(req, new String[]{"id"});
		if(!respcode.equals(RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC))) {
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(respcode));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getMsgByCode(respcode));
			return result;
		}
		result = orderSrv.refund(req.get("id").toString());
		return result;
	}
	
	/**
	 * 更新发货状态
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping("updDelvStatu")
	@ResponseBody
	public Map<String,Object> updateOrderStatus(HttpServletRequest request,@RequestBody Map<String,Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			List<String> orderIds = (List<String>)req.get("orders");
			orderSrv.updateDeliverStatus(orderIds);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SUC));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
		}
		
		return result;
	}
	
	@RequestMapping("refundNotify")
	@ResponseBody
	public Map<String,Object> refundNotify(HttpServletRequest request,String source,String app_id,String out_order_number,
			String order_number,String refund_price,String refund_date,String order_status,String sign){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("source", source);
		params.put("app_id", app_id);
		params.put("out_order_number", out_order_number);
		params.put("order_number", order_number);
		params.put("refund_price", refund_price);
		params.put("refund_date", refund_date);
		params.put("order_status", order_status);
		params.put("sign", sign);
		return orderSrv.notifyHandler(params);
	}
}
