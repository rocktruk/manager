package com.online.mall.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.manager.repository.RcmndCommdyRepository;

@Service
public class RcmndCommdyService extends AbstractMallService {

	@Autowired
	private RcmndCommdyRepository rcmndRepository;
	
	
	public List<String> hotSale(){
		return rcmndRepository.findAllHotSale();
	}
	
	
	public List<String> getRcmndCommdy(){
		return rcmndRepository.findAllRcmnd();
	}
	
}
