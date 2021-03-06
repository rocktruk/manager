package com.online.mall.manager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.common.ConfigConstants;
import com.online.mall.manager.common.DictConstantsUtil;
import com.online.mall.manager.entity.Trans;
import com.online.mall.manager.repository.TransRepository;

@Service
public class TransactionService {

	@Autowired
	private TransRepository transRepo;
	
	@Transactional
	public void saveTransEntity(Trans entity) {
		transRepo.save(entity);
	}
	
	
	public Optional<Trans> getTransByOrderNum(String backChannel,String backChnlTraceNo){
		return transRepo.findTransByBackChnlTraceNoAndBackChannelAndTrxCode(backChannel, backChnlTraceNo,DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRXCODE_REFUND));
	}
	
	
	public Optional<Trans> getTransById(String traceNo){
		return transRepo.findById(traceNo);
	}
	
	
}
