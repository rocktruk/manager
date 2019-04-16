package com.online.mall.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.online.mall.manager.entity.RecommendCommodities;

@Repository
public interface RcmndCommdyRepository extends IExpandJpaRepository<RecommendCommodities, String> {

	
	@Query("select s from RecommendCommodities s where s.rcmndType = '1'")
	List<String> findAllHotSale();
	
	
	@Query("select s from RecommendCommodities s where s.rcmndType = '2'")
	List<String> findAllRcmnd();
}
