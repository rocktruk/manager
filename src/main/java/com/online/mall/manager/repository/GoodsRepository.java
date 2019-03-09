package com.online.mall.manager.repository;

import java.math.BigDecimal;

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
	
}
