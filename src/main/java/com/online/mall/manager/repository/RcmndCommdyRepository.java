package com.online.mall.manager.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.RecommendCommodities;

@Repository
public interface RcmndCommdyRepository extends IExpandJpaRepository<RecommendCommodities, String> {

	
	@Query("select s.goods.id from RecommendCommodities s where s.rcmndType = '1'")
	List<String> findAllHotSale();
	
	
	@Query("select s.goods.id from RecommendCommodities s where s.rcmndType = '2'")
	List<String> findAllRcmnd();
	
	@Query("select s.id,s.goods.id from RecommendCommodities s where s.rcmndType = '1'")
	List<Object[]> findAllComboIdWithHotSale();
	
	@Modifying
	@Transactional
	@Query("update RecommendCommodities r set r.rcmndImg = ?1 where r.id=?2")
	int updateRecommendCommoditiesSetRcmndImgById(String imgPath,String id);
	
	@Modifying
	@Transactional
	@Query("delete RecommendCommodities r where r.goods.id in (?1)")
	int delRecommendCommoditiesByGoodsId(Collection<String> id);
}
