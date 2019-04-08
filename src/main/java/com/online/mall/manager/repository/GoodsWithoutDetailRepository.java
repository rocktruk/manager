package com.online.mall.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online.mall.manager.entity.GoodsWithoutDetail;

public interface GoodsWithoutDetailRepository extends IExpandJpaRepository<GoodsWithoutDetail, String> {

	
}
