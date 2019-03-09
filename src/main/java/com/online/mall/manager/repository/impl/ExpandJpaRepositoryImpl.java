package com.online.mall.manager.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.online.mall.manager.repository.IExpandJpaRepository;


public class ExpandJpaRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IExpandJpaRepository<T, ID> {

	public ExpandJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}

	private final EntityManager entityManager;

	

}
