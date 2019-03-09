package com.online.mall.manager.repository.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.online.mall.manager.repository.impl.ExpandJpaRepositoryImpl;

public class ExpandJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID> extends JpaRepositoryFactoryBean<T, S, ID> {


	public ExpandJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		// TODO Auto-generated method stub
		return new ExpandJpaRepositoryFactory(entityManager);
	}
	
	private static class ExpandJpaRepositoryFactory<T,ID> extends JpaRepositoryFactory
	{
		private final EntityManager entityManager;
		
		public ExpandJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}
		
//		protected Object getTargetRepository(RepositoryMetadata metadata) {
//			JpaEntityInformation<?,Object> entityInfomation = (JpaEntityInformation<?,Object>)getEntityInformation(metadata.getDomainType())
//			return new ExpandJpaRepositoryImpl(entityInfomation, entityManager);
//		}
	
		@Override
		protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
				EntityManager entityManager) {
			return new ExpandJpaRepositoryImpl<T, Serializable>((Class<T>)information.getDomainType(), entityManager);
		}
		 
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata)
		{
			return ExpandJpaRepositoryImpl.class;
		}
	}
}
