package com.online.mall.manager.trans.service;

public class RefundNotifyRequest {

	
	private String source;
	
	private String app_id;
	
	private String out_order_number;
	
	private String order_number;
	
	private String refund_price;
	
	private String refund_date;
	
	private String order_status;

	private String sign;
	
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

	public String getOut_order_number() {
		return out_order_number;
	}

	public void setOut_order_number(String out_order_number) {
		this.out_order_number = out_order_number;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getRefund_price() {
		return refund_price;
	}

	public void setRefund_price(String refund_price) {
		this.refund_price = refund_price;
	}

	public String getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}
