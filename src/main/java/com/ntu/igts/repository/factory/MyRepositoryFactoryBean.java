package com.ntu.igts.repository.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class MyRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends
                JpaRepositoryFactoryBean<R, T, I> {

    @SuppressWarnings("rawtypes")
    @Override
    public RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        return new MyRepositoryFactory(entityManager);
    }

}
