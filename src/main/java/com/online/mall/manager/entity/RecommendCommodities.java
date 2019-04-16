package com.online.mall.manager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="RCMND_COMDITIS")
@Entity
public class RecommendCommodities {

	@Id
	private String id;

	@Column(name="RCMND_TYPE")
	private String rcmndType;
	
	@Column(name="RCMND_IMG")
	private String rcmndImg;
	
	@Column(name="CREATE_TIME",insertable=false,updatable=false)
	private Date createTime;
	
	@OneToOne
	@JoinColumn(name="GODS_ID")
	private GoodsWithoutDetail goods;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRcmndType() {
		return rcmndType;
	}

	public void setRcmndType(String rcmndType) {
		this.rcmndType = rcmndType;
	}

	public String getRcmndImg() {
		return rcmndImg;
	}

	public void setRcmndImg(String rcmndImg) {
		this.rcmndImg = rcmndImg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public GoodsWithoutDetail getGoods() {
		return goods;
	}

	public void setGoods(GoodsWithoutDetail goods) {
		this.goods = goods;
	}
	
}
