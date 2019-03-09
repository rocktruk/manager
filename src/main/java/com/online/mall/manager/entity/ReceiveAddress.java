package com.online.mall.manager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="RECEIVE_ADDRESS")
@Entity
public class ReceiveAddress {

	@Id
	private String id;
	
	@Column(name="CUS_ID",nullable=false)
	private long cusId;
	
	@Column(name="RECV_NAME")
	private String recvName;
	
	@Column(name="PHONE",nullable=false)
	private String phone;
	
	@Column(name="PROVICE")
	private String provice;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="COUNTY")
	private String county;
	
	@Column(name="DETAILED_ADDRESS")
	private String detailedAddr;
	
	@Column(name="LST_UPD_TIME")
	private Date lstUpdTime;
	
	@Column(name="IS_DFT_ADDR",columnDefinition="char",length=1,nullable=false)
	private String dftAddr;

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

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDetailedAddr() {
		return detailedAddr;
	}

	public void setDetailedAddr(String detailedAddr) {
		this.detailedAddr = detailedAddr;
	}

	public Date getLstUpdTime() {
		return lstUpdTime;
	}

	public void setLstUpdTime(Date lstUpdTime) {
		this.lstUpdTime = lstUpdTime;
	}

	public String getDftAddr() {
		return dftAddr;
	}

	public void setDftAddr(String dftAddr) {
		this.dftAddr = dftAddr;
	}
	
	
	
}
