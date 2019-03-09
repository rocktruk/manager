package com.online.mall.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.manager.entity.GoodsMenu;

@Repository
public interface GoodsMenuRepository extends IExpandJpaRepository<GoodsMenu, Integer> {

	
	@Transactional
	@Modifying
	@Query(value="insert into GOODS_MENU value (?,?,?,?)",nativeQuery = true)
	int addMenu(int id,int parentId,String menuName,String imageSrc);
	
	@Query("select g from GoodsMenu g where g.menuName = ?1")
	Optional<GoodsMenu> findGoodsMenuByMenuName(String menuName);
	
	
}
