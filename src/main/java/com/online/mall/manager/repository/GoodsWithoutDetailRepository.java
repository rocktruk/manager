package com.online.mall.manager.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.online.mall.manager.entity.GoodsWithoutDetail;

public interface GoodsWithoutDetailRepository extends IExpandJpaRepository<GoodsWithoutDetail, String> {

	
	@Query("select g.id,g.title from GoodsWithoutDetail g where g.status = '2'")
	List<GoodsWithoutDetail> findAllGoodsPutAway();
	
}
