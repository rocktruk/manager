package com.online.mall.manager.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="TRANS")
@Entity
public class Trans {

	@Id
	@Column(name="TRACE_NO")
	private String traceNo;
	
	@Column(name="TRX_STATUS")
	private String trxStatus;
	
	@Column(name="BACK_CHANNEL")
	private String backChannel;
	

	@Column(name="TRX_AMT")
	private BigDecimal trxAmt; 
	
	@Column(name="CUS_ID")
	private long cusId;
	
	@Column(name="ORI_TRACE_NO")
	private String oriTraceNo;
	
	@Column(name="TRX_CODE")
	private String trxCode;
	
	@Column(name="REFUNDED_AMT")
	private BigDecimal refundedAmt;
	
	@Column(name="REFUNDABLE_AMT")
	private BigDecimal refundableAmt;
	
	@Column(name="BACK_CHNL_TRACE_NO")
	private String backChnlTraceNo;
	
	@Column(name="CREATE_TIME",insertable=false,updatable=false)
	private Date createTime;
	
	@Column(name="LST_UPD_TIME",insertable=false,updatable=false)
	private Date LST_UPD_TIME;

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public long getCusId() {
		return cusId;
	}

	public void setCusId(long cusId) {
		this.cusId = cusId;
	}

	public String getOriTraceNo() {
		return oriTraceNo;
	}

	public void setOriTraceNo(String oriTraceNo) {
		this.oriTraceNo = oriTraceNo;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public BigDecimal getRefundedAmt() {
		return refundedAmt;
	}

	public void setRefundedAmt(BigDecimal refundedAmt) {
		this.refundedAmt = refundedAmt;
	}

	public BigDecimal getRefundableAmt() {
		return refundableAmt;
	}

	public void setRefundableAmt(BigDecimal refundableAmt) {
		this.refundableAmt = refundableAmt;
	}

	public String getBackChnlTraceNo() {
		return backChnlTraceNo;
	}

	public void setBackChnlTraceNo(String backChnlTraceNo) {
		this.backChnlTraceNo = backChnlTraceNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLST_UPD_TIME() {
		return LST_UPD_TIME;
	}

	public void setLST_UPD_TIME(Date lST_UPD_TIME) {
		LST_UPD_TIME = lST_UPD_TIME;
	}
	
	public String getBackChannel() {
		return backChannel;
	}

	public void setBackChannel(String backChannel) {
		this.backChannel = backChannel;
	}
	
}
