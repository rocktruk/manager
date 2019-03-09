package com.online.mall.manager.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.MasterTrans;

@Repository
public interface MasterTransRepository extends IExpandJpaRepository<MasterTrans, String>{

	
	@Modifying
	@Transactional
	@Query(value="insert into TRANS value (?,?,?,?,?,?,?,?,?,?)",nativeQuery = true)
	int addTrans(String traceNo,String trxStatus,String backChannel,BigDecimal trxAmt,long cusId,String oriTraceNo,String trxCode,BigDecimal refundedAmt,BigDecimal refundableAmt,String backChnlTraceNo);
	
	
	
}
