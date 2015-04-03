package com.ntu.igts.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
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
        QueryField queryFieldAnnotation = this.domainClass.getAnnotation(QueryField.class);
        final String[] queryFields = queryFieldAnnotation == null ? new String[0] : queryFieldAnnotation.value();
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
                        query.where(cb.and(cb.or(predicateList.toArray(new Predicate[predicateList.size()])),
                                        cb.equal(deletedYN, "N")));
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

    @Override
    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum) {
        return findAll(sortByEnum, orderByEnum, null, false);
    }

    @Override
    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, boolean isIncludeDeleted) {
        return findAll(sortByEnum, orderByEnum, null, isIncludeDeleted);
    }

    @Override
    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, Map<String, String> criteriaMap) {
        return findAll(sortByEnum, orderByEnum, criteriaMap, false);
    }

    @Override
    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, Map<String, String> criteriaMap,
                    boolean isIncludeDeleted) {
        final String soryBy = sortByEnum.value();
        final String orderBy = orderByEnum.value();
        final Map<String, String> queryCriteriaMap = criteriaMap;
        if (isIncludeDeleted) {
            return super.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = getPredicateListByCriteriaMap(root, cb, queryCriteriaMap);
                    if (OrderByEnum.ASC.equals(orderBy)) {
                        query.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()]))).orderBy(
                                        cb.asc(root.get(soryBy)));
                    } else {
                        query.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()]))).orderBy(
                                        cb.desc(root.get(soryBy)));
                    }
                    return null;
                }
            });
        } else {
            return super.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = getPredicateListByCriteriaMap(root, cb, queryCriteriaMap);
                    Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                    if (OrderByEnum.ASC.equals(orderBy)) {
                        query.where(cb.and(cb.and(predicateList.toArray(new Predicate[predicateList.size()])),
                                        cb.equal(deletedYN, "N"))).orderBy(cb.asc(root.get(soryBy)));
                    } else {
                        query.where(cb.and(cb.and(predicateList.toArray(new Predicate[predicateList.size()])),
                                        cb.equal(deletedYN, "N"))).orderBy(cb.desc(root.get(soryBy)));
                    }
                    return null;
                }
            });
        }
    }

    private List<Predicate> getPredicateListByCriteriaMap(Root<T> root, CriteriaBuilder cb,
                    final Map<String, String> criteriaMap) {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (criteriaMap != null) {
            for (Entry<String, String> entry : criteriaMap.entrySet()) {
                Path<String> queryPath = root.get(entry.getKey());
                predicateList.add(cb.equal(queryPath, entry.getValue()));
            }
        }
        return predicateList;
    }

    @Override
    public Page<T> findByPage(int currentPage, int pageSize, SortByEnum sortByEnum, OrderByEnum orderByEnum,
                    Map<String, String> criteriaMap) {
        return findByPage(currentPage, pageSize, sortByEnum, orderByEnum, criteriaMap, false);
    }

    @Override
    public Page<T> findByPage(int currentPage, int pageSize, SortByEnum sortByEnum, OrderByEnum orderByEnum,
                    Map<String, String> criteriaMap, boolean isIncludeDeleted) {
        if (orderByEnum == null) {
            orderByEnum = OrderByEnum.DESC;
        }
        final Map<String, String> queryCriteriaMap = criteriaMap;
        Sort sort = new Sort(orderByEnum.toDirectionEnum(), sortByEnum.value());
        Pageable pageable = new PageRequest(currentPage, pageSize, sort);
        if (isIncludeDeleted) {
            return super.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = getPredicateListByCriteriaMap(root, cb, queryCriteriaMap);
                    query.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
                    return null;
                }
            }, pageable);
        } else {
            return super.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicateList = getPredicateListByCriteriaMap(root, cb, queryCriteriaMap);
                    Path<String> deletedYN = root.get(Constants.FIELD_DELETED_YN);
                    query.where(cb.and(cb.and(predicateList.toArray(new Predicate[predicateList.size()])),
                                    cb.equal(deletedYN, "N")));
                    return null;
                }
            }, pageable);
        }
    }

}
