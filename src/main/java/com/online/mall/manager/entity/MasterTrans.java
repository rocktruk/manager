package com.online.mall.manager.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="TRANS")
@Entity
public class MasterTrans {

	@Id
	@Column(name="TRACE_NO",columnDefinition="varchar",length=40,unique=true)
	private String traceNo;
	
	@Column(name="TRX_STATUS",columnDefinition="char",length=2,nullable=false)
	private String trxStatus;
	
	@Column(name="BACK_CHANNEL",columnDefinition="char",length=2)
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

	public String getBackChannel() {
		return backChannel;
	}

	public void setBackChannel(String backChannel) {
		this.backChannel = backChannel;
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
	
	
}
