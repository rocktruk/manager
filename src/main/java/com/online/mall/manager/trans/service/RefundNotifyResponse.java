package com.online.mall.manager.trans.service;

import java.util.Map;

public class RefundNotifyResponse {

	private int status;
	
	private String msg;
	
	private Map<String,String> data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	
	
}
