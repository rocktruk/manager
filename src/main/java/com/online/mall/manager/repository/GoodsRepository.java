package com.online.mall.manager.repository;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.Goods;

@Repository
public interface GoodsRepository extends IExpandJpaRepository<Goods, String> {

	@Modifying
	@Transactional
	@Query(value="insert into goods value (?,?,?,?,?,?,?,?,?,?,?,?)",nativeQuery=true)
	int insertGoods(String id,BigDecimal price,String brand,byte[] detail,long inventory,String status,String imgPath,int goodsMenuId,String specification,String title,long monthSales,long totalSales);
	
	@Modifying
	@Transactional
	@Query("update Goods g set g.imgPath = ?2 where g.id = ?1")
	int updateGoodsImgWithId(String id,String imgPath);
	
	
	@Modifying
	@Transactional
	@Query("update Goods g set g.banerImages = ?2 where g.id = ?1")
	int updateGoodsBannersWithId(String id,String imgPath);
	
	@Modifying
	@Transactional
	@Query("update Goods g set g.status = ?2 where g.id = ?1")
	int updateGoodsSetStatusWithId(String id,String status);
	
	
	@Modifying
	@Transactional
	@Query(value="update Goods g set g.status = ?1 where g.id in (?2)")
	int updateGoodsSetStatusByIdIn(String status,Collection<String> id);
}
