package com.ntu.igts.repository.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.ntu.igts.repository.MyRepository;
import com.ntu.igts.repository.impl.MyRepositoryImpl;

public class MyRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

    private EntityManager entityManager;

    public MyRepositoryFactory(EntityManager entityManager) {
        super(entityManager);

        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {

        return new MyRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

        // The RepositoryMetadata can be safely ignored, it is used by the JpaRepositoryFactory
        // to check for QueryDslJpaRepository's which is out of scope.
        return MyRepository.class;
    }
}
