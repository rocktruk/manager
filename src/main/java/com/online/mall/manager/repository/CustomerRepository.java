package com.online.mall.manager.repository;


import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.Customer;

@Repository
public interface CustomerRepository extends IExpandJpaRepository<Customer, Long> {

	
	@Modifying
	@Transactional
	@Query(value="insert into Custom value(?,?,?,?,?)",nativeQuery = true)
	int addCustomer(long id,String channelType,String name,String openId,Date lstUpdTime);
	
	@Query("select c from Customer c where c.openId = ?1 and c.channelType = ?2")
	Optional<Customer> getCustomerWithOpenId(String openId,String channelType);
}
