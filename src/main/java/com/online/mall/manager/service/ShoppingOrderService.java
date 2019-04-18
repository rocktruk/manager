package com.online.mall.manager.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.online.mall.manager.common.CacheImpl;
import com.online.mall.manager.common.CacheUtil;
import com.online.mall.manager.common.ConfigConstants;
import com.online.mall.manager.common.DictConstantsUtil;
import com.online.mall.manager.common.HttpUtil;
import com.online.mall.manager.common.IConstants;
import com.online.mall.manager.common.IdGenerater;
import com.online.mall.manager.common.RespConstantsUtil;
import com.online.mall.manager.common.SignatureUtil;
import com.online.mall.manager.entity.ShoppingOrder;
import com.online.mall.manager.entity.Trans;
import com.online.mall.manager.repository.ShoppingOrderRepository;
import com.online.mall.manager.trans.service.OrderStatusUpdateRequest;
import com.online.mall.manager.trans.service.OrderStatusUpdateResponse;

@Service
public class ShoppingOrderService extends AbstractMallService{

	private static final Logger log = LoggerFactory.getLogger(ShoppingOrderService.class);
	
	@Autowired
	private ShoppingOrderRepository orderRepos;
	
	@Autowired
	private TransactionService transService;
	
	@Autowired
	private CacheUtil cache;
	
	@Resource
	private ApplicationContext context;
	
	@Value(value="${appid}")
	private String appId;
	
	@Value(value="${signkey}")
	private String signkey;
	
	@Value(value="${order.status.update}")
	private String ordrUpdUrl;
	
	public static final Map<Integer,String> orderColumn;
	static {
		orderColumn = new HashMap<Integer, String>();
		orderColumn.put(1, "goods.title");
		orderColumn.put(2, "user.name");
		orderColumn.put(3, "trans.traceNo");
		orderColumn.put(4, "trans.backChnlTraceNo");
		orderColumn.put(6, "orderStatus");
	}
	
	
	public String orderGoods(int orderSeq) {
		return orderColumn.get(orderSeq);
	}
	
	
	@Transactional
	public void saveOrders(List<ShoppingOrder> orders) {
		orderRepos.saveAll(orders);
	}
	
	public List<ShoppingOrder> getOrdersByTrans(String traceNo){
		return orderRepos.findShoppingOrderByTransNo(traceNo);
	}
	
	
	public Optional<ShoppingOrder> findShoppingOrderbyId(String id){
		return orderRepos.findShoppingOrderById(id);
	}
	
	/**
	 * 发起退款请求
	 * @param id
	 * @return
	 */
	public Map<String,Object> refund(String id){
		Map<String,Object> result = new HashMap<String, Object>();
		Optional<ShoppingOrder> order = findShoppingOrderbyId(id);
		if(order.isPresent())
		{
			String sucOrdrStatus = DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_SUC);
			if(!sucOrdrStatus.equals(order.get().getOrderStatus()) && order.get().getTrans().getRefundableAmt().compareTo(order.get().getPayAmt())==-1) {
				log.error("退款条件不满足|当前订单状态："+order.get().getOrderStatus()+"|可退金额："+order.get().getTrans().getRefundableAmt()+"|本次退款金额："+order.get().getPayAmt());
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_REFUND_FAIL));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_REFUND_FAIL));
				return result;
			}
			Object o = cache.putIfAbsent(CacheImpl.Caches.prevent_repeat.name(), id, "1");
			if(o!=null) {
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_REFUND_FAIL));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_REFUND_FAIL));
				return result;
			}
			OrderStatusUpdateRequest req = new OrderStatusUpdateRequest();
			req.setApp_id(appId);
			req.setOpen_userid(order.get().getUser().getOpenId());
			req.setOrder_number(order.get().getTrans().getBackChnlTraceNo());
			req.setOrder_status(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDRTYPE_UPD_REFUND));
			req.setRefund_price(order.get().getPayAmt().toString());
			req.setSource(order.get().getUser().getChannelType());
			String traceNo = IdGenerater.INSTANCE.transIdGenerate();
			try {
				String response = HttpUtil.post(ordrUpdUrl, req.pack());
				log.warn("退款返回报文："+response);
				OrderStatusUpdateResponse resp = JSON.parseObject(response, OrderStatusUpdateResponse.class);
				//插入交易流水
				Trans entity = new Trans();
				entity.setTraceNo(traceNo);
				entity.setCusId(order.get().getUser().getId());
				entity.setRefundableAmt(order.get().getTrans().getRefundableAmt().subtract(order.get().getPayAmt()));
				entity.setRefundedAmt(order.get().getPayAmt());
				entity.setTrxAmt(order.get().getPayAmt());
				entity.setTrxCode(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRXCODE_REFUND));
				entity.setTrxStatus(1==resp.getStatus()?DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL):DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_INITIAL));
				entity.setBackChnlTraceNo(resp.getData().getOrder_number());
				entity.setBackChannel(order.get().getUser().getChannelType());
				entity.setOriTraceNo(order.get().getTrans().getTraceNo());
				entity.setRefTraceNo(order.get().getId());
				transService.saveTransEntity(entity);
				if(0==resp.getStatus()) {
					order.get().setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_REFUNDING));
					//设置退款交易流水号
					order.get().setRefTraceNo(traceNo);
					orderRepos.save(order.get());
				}
			} catch (IOException e) {
				log.error(e.getMessage(),e);
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SYSERR));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SYSERR));
			}
		}else {
			result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_ORDER_NOTEXIST));
			result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_ORDER_NOTEXIST));
		}
		return result;
	}
	
	
	/**
	 * 获取已付款待收货的订单
	 * @param cusId
	 * @param start
	 * @param length
	 * @return
	 */
	public List<ShoppingOrder> findOrderByWaitCollect(long cusId,int start,int length){
		return orderRepos.findShoppingOrderByStatusWithPage(cusId,start*length,length);
	}
	
	public Map<String,Object> searchOrder(Sort sort,int index,int length){
		return searchOrder(null, sort, index, length);
	}
	
	
	public Map<String,Object> searchOrder(ShoppingOrder order,Sort sort,int index,int length){
		Map<String,Object> result = new HashMap<String, Object>();
		List<ShoppingOrder> orders = findOrderWithPage(order,sort, index, length);
		long total = orderRepos.count();
		result.put("recordsTotal", total);
		result.put("recordsFiltered", total);
		result.put("data", orders);
		return result;
	}
	
	/**
	 * 根据请求条件分页查询订单
	 * @param order
	 * @param sort
	 * @param index
	 * @param length
	 * @return
	 */
	public List<ShoppingOrder> findOrderWithPage(ShoppingOrder order,Sort sort,int index,int length){
		if(order == null) {
			order = new ShoppingOrder();
		}
		ExampleMatcher matcher = ExampleMatcher.matching();
		Example<ShoppingOrder> example = Example.of(order,matcher);
		PageRequest page = null;
		if(sort == null)
		{
			page = PageRequest.of(index, length);
		}else
		{
			page = PageRequest.of(index, length, sort);
		}
		return orderRepos.findAll(example, page).getContent();
	}
	
	
	/**
	 * 退款通知处理，更新退款交易流水，消费交易流水，订单
	 * @param params
	 * @return
	 */
	public Map<String,Object> notifyHandler(Map<String,Object> params){
		Map<String,Object> result = new HashMap<String, Object>(); 
		try {
			boolean flag = SignatureUtil.INTANCE.checkSign(params);
			if(!flag) {
				result.put(IConstants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPCODE_SIGNCHECK_ERROR));
				result.put(IConstants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IConstants.RESPMSG_SIGNCHECK_ERROR));
				return result;
			}
			Optional<Trans> trans = transService.getTransByOrderNum(params.get("order_number").toString(),params.get("source").toString());
			if(trans.isPresent()) {
				Optional<ShoppingOrder> order =orderRepos.findById(trans.get().getRefTraceNo());
				//科匠退款成功，订单状态为7
				if("7".equals(params.get("order_status"))) {
					trans.get().setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_SUC));
					transService.saveTransEntity(trans.get());
					Optional<Trans> oriTrans = transService.getTransById(trans.get().getOriTraceNo());
					oriTrans.ifPresent(t -> {
						//更新原交易流水，可退金额，已退金额，交易状态，可退金额为0，则为全部退款
						t.setRefundableAmt(t.getRefundableAmt().subtract(trans.get().getTrxAmt()));
						t.setRefundedAmt(t.getRefundedAmt().add(trans.get().getTrxAmt()));
						if(BigDecimal.ZERO.compareTo(t.getRefundableAmt())==0) {
							t.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_REFUNDED));
						}else {
							t.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_PARTIALREFUND));
						}
						transService.saveTransEntity(t);
					});
					order.ifPresent(o -> {
						o.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_REFUND));
						orderRepos.save(o);
					});
				}else {
					trans.ifPresent(t -> {
						t.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL));
						transService.saveTransEntity(t);
					});
					order.ifPresent(o -> {
						o.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_FAILED));
						//退款失败，退款流水取消关联
						o.setRefTraceNo("");
						orderRepos.save(o);
					});
				}
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		
		return result;
	}
	
}
