package com.online.mall.manager.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="SHOP_ORDER")
@Entity
public class ShoppingOrder {

	@Id
	private String id;
	
	@Column(name="CUS_ID")
	private long cusId;
	
	@Column(name="GOODS_ID")
	private String goodsId;
	
	@Column(name="TRANS_NO")
	private String transNo;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="LST_UPD_DATE")
	private Date lstUpdDate;
	
	@Column(name="DELIVER_STATUS")
	private String deliverStatus;
	
	@Column(name="ADDRESS_ID")
	private String addressId;
	
	@Column(name="COUNT")
	private String count;
	
	@Column(name="TOTAL_ORDR_AMT")
	private BigDecimal totalOrdrAmt;
	
	@Column(name="DISCOUNT_AMT")
	private BigDecimal discountAmt;
	
	@Column(name="PAY_AMT")
	private BigDecimal payAmt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCusId() {
		return cusId;
	}

	public void setCusId(long cusId) {
		this.cusId = cusId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getLstUpdDate() {
		return lstUpdDate;
	}

	public void setLstUpdDate(Date lstUpdDate) {
		this.lstUpdDate = lstUpdDate;
	}

	public String getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public BigDecimal getTotalOrdrAmt() {
		return totalOrdrAmt;
	}

	public void setTotalOrdrAmt(BigDecimal totalOrdrAmt) {
		this.totalOrdrAmt = totalOrdrAmt;
	}

	public BigDecimal getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(BigDecimal discountAmt) {
		this.discountAmt = discountAmt;
	}

	public BigDecimal getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	
	
	
	
}
