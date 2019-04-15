package com.online.mall.manager.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.online.mall.manager.repository.RcmndCommdyRepository;

public class RcmndCommdyService extends AbstractMallService {

	@Autowired
	private RcmndCommdyRepository rcmndRepository;
}
