package com.online.mall.manager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.manager.entity.MerAdmin;
import com.online.mall.manager.repository.MerAdminRepository;

@Service
public class MerAdminService extends AbstractMallService {

	@Autowired
	private MerAdminRepository merRepository;
	
	
	public Optional<MerAdmin> findMerById(long id) {
		return merRepository.findById(id);
	}
}
