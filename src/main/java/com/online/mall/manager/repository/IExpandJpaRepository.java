package com.online.mall.manager.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IExpandJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	
}
