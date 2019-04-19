package com.online.mall.manager.repository;

import java.util.Optional;

import com.online.mall.manager.entity.MerAdmin;

public interface MerAdminRepository extends IExpandJpaRepository<MerAdmin, Long> {

	
	
	Optional<MerAdmin> findMerAdminByAcctName(String acctName);
	
}
