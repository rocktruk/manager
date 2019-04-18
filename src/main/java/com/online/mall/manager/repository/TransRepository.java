package com.online.mall.manager.repository;

import java.util.Optional;

import com.online.mall.manager.entity.Trans;

public interface TransRepository extends IExpandJpaRepository<Trans, String> {

	Optional<Trans> findTransByBackChnlTraceNoAndBackChannelAndTrxCode(String backChannel,String backChnlTraceNo,String trxCode);
	
}
