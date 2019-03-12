package com.online.mall.manager.trans.service;

import org.springframework.beans.factory.annotation.Value;

public class OrderStatusUpdateRequest {

	
	private String source;
	
	@Value(value="${appid}")
	private String app_id;
	
	private String open_userid;
	
	
	private String order_number;
	
	private String order_status;
	
	private String refund_price;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getOpen_userid() {
		return open_userid;
	}

	public void setOpen_userid(String open_userid) {
		this.open_userid = open_userid;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getRefund_price() {
		return refund_price;
	}

	public void setRefund_price(String refund_price) {
		this.refund_price = refund_price;
	}
	
	
	
}
