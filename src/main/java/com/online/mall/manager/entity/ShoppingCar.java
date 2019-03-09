package com.online.mall.manager.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="SHOPPING_CAR")
@Entity
public class ShoppingCar {

	
	@Id
	@Column(name="ID",columnDefinition="varchar",length=20)
	private String id;
	
	@Column(name="CUS_ID",nullable=false)
	private long cusId;
	
	@Column(name="CREATE_TIME",columnDefinition="timestamp")
	private Date createTime;
	
	@Column(name="CURRENT_PRICE")
	private BigDecimal currentPrice;
	
	@Column(name="GOODS_ID",columnDefinition="varchar",length=20)
	private String goodsId;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
	
	
}
