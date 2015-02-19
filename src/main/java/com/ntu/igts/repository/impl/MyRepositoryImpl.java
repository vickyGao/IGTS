package com.ntu.igts.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.annotations.QueryField;
import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.BaseModel;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.MyRepository;
import com.ntu.igts.utils.StringUtil;

@NoRepositoryBean
public class MyRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
                MyRepository<T, ID> {
    private EntityManager entityManager;
    private Class<T> domainClass;

    // There are two constructors to choose from, either can be used.
    public MyRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);

        // This is the recommended method for accessing inherited class dependencies.
        this.entityManager = entityManager;
        this.domainClass = domainClass;
    }

    @Transactional
    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    @Override
    public void delete(final ID id) {
        final T entity = findById(id);
        if (entity == null) {
            return;
        }
        if (entity instanceof BaseModel) {
            BaseModel model = (BaseModel) entity;
            model.setDeletedYN("Y");
            entityManager.merge(model);
        }
    }

    @Transactional
    @Override
    public void delete(ID id, boolean isLogicDelete) {
        if (isLogicDelete) {
            delete(id);
        } else {
            final T entity = super.findOne(id);
            if (entity == null) {
                return;
            }
            entityManager.remove(entity);
        }

    }

    @Override
    public T findById(final ID id) {
        return findById(id, false);

    }

    @Override
    public T findById(final ID id, boolean isIncludeDeleted) {
        if (isIncludeDeleted) {
            return super.findOne(id);
        } else {
            return super.findOne(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Path<ID> primaryKey = root.get(Constants.FIELD_ID);
                    Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                    query.where(cb.equal(primaryKey, id), cb.equal(deletedYN, "N"));
                    return null;
                }
            });
        }
    }

    @Override
    public List<T> findAll(boolean isIncludeDeleted) {
        if (isIncludeDeleted) {
            return super.findAll();
        } else {
            return super.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                    query.where(cb.equal(deletedYN, "N"));
                    return null;
                }
            });
        }
    }

    @Override
    public List<T> findAll() {
        return findAll(false);
    }

    @Override
    public Page<T> findByPage(Query query) {
        return findByPage(query, false);
    }

    @Override
    public Page<T> findByPage(Query query, boolean isIncludeDeleted) {
        final String searchTerm = query.getSearchTerm();
        final String[] queryFields = this.domainClass.getAnnotation(QueryField.class).value();
        Sort sort = new Sort(query.getOrderBy().toDirectionEnum(), query.getSortBy().value());
        Pageable pageable = new PageRequest(query.getPage(), query.getSize(), sort);
        if (isIncludeDeleted) {
            if (!StringUtil.isEmpty(searchTerm)) {
                return super.findAll(new Specification<T>() {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        List<Predicate> predicateList = new ArrayList<Predicate>();
                        for (String queryField : queryFields) {
                            Path<String> queryPath = root.get(queryField);
                            predicateList.add(cb.like(queryPath, "%" + StringUtils.trim(searchTerm) + "%"));
                        }
                        query.where(cb.or(predicateList.toArray(new Predicate[predicateList.size()])));
                        return null;
                    }
                }, pageable);
            } else {
                return super.findAll(pageable);
            }

        } else {
            if (!StringUtil.isEmpty(searchTerm)) {
                return super.findAll(new Specification<T>() {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        List<Predicate> predicateList = new ArrayList<Predicate>();
                        for (String queryField : queryFields) {
                            Path<String> queryPath = root.get(queryField);
                            predicateList.add(cb.like(queryPath, "%" + StringUtils.trim(searchTerm) + "%"));
                        }
                        Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                        query.where(cb.or(predicateList.toArray(new Predicate[predicateList.size()])),
                                        cb.equal(deletedYN, "N"));
                        return null;
                    }
                }, pageable);
            } else {
                return super.findAll(new Specification<T>() {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                        Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                        query.where(cb.equal(deletedYN, "N"));
                        return null;
                    }
                }, pageable);
            }

        }
    }

}
