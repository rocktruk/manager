package com.online.mall.manager.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.ShoppingOrder;

@Repository
public interface ShoppingOrderRepository extends IExpandJpaRepository<ShoppingOrder, String> {

	@Modifying
	@Transactional
	@Query(value="insert into shop_order(id,cus_id,goods_id,trans_no,order_status,deliver_status,address_id,count,total_ordr_amt,discount_amt,pay_amt)"
			+ "value (?,?,?,?,?,?,?,?,?,?)",nativeQuery=true)
	int insertOrder(String id,long cusId, String goodsId,String transNo,String orderStatus,String deliverStatus,
			String addressId,BigDecimal totalOrdrAmt,BigDecimal discountAmt,BigDecimal payAmt);
	
	@Query("select s from ShoppingOrder s where s.trans.traceNo = ?1")
	List<ShoppingOrder> findShoppingOrderByTransNo(String traceNo);
	
	@Query(value = "select * from shop_order s where s.CUS_ID = ?1 and s.ORDER_STATUS = '00' and s.DELIVER_STATUS != '03' order by s.CREATE_TIME limit ?2,?3",nativeQuery=true)
	List<ShoppingOrder> findShoppingOrderByStatusWithPage(long cusId,int start,int length);
	
	
	Optional<ShoppingOrder> findShoppingOrderById(String id);
	
	Optional<ShoppingOrder> findShoppingOrderByRefTraceNo(String reftraceNo);
}
