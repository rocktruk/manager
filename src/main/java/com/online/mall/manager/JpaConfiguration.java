package com.online.mall.manager;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.online.mall.manager.repository.factory.ExpandJpaRepositoryFactoryBean;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableJpaRepositories(basePackages = {
        "com.online.mall.manager.repository"
},repositoryFactoryBeanClass=ExpandJpaRepositoryFactoryBean.class)
@EnableTransactionManagement(proxyTargetClass=true)
@EntityScan(basePackages="com.online.mall.manager.entity")
public class JpaConfiguration {

	@Bean
    PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
	
}
