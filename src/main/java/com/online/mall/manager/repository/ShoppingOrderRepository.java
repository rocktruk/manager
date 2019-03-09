package com.online.mall.manager.repository;

import org.springframework.stereotype.Repository;

import com.online.mall.manager.entity.ShoppingOrder;

@Repository
public interface ShoppingOrderRepository extends IExpandJpaRepository<ShoppingOrder, String> {

}
