package com.ntu.igts.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.container.Query;

@NoRepositoryBean
public interface MyRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    public T create(T entity);

    public T update(T entity);

    @Override
    public void delete(final ID id);

    public void delete(final ID id, boolean isLogicDelete);

    @Override
    public List<T> findAll();

    public List<T> findAll(boolean isIncludeDeleted);

    public T findById(final ID id);

    public T findById(final ID id, boolean isIncludeDeleted);

    /**
     * Get by search term with criteria
     * 
     * @param searchTerm
     *            The search term
     * @param page
     *            The page number (start from 0)
     * @param size
     *            The size of each page
     * @param sortBy
     *            Sort by (upper case letter of the field in model)
     * @param orderBy
     *            Order by (ASC, DESC)
     * @return The Page object
     */
    public Page<T> findByPage(Query query);

    public Page<T> findByPage(Query query, boolean isIncludeDeleted);

    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum);

    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, boolean isIncludeDeleted);

    /**
     * Get with criteria
     * 
     * @param sortByEnum
     *            The sort field
     * @param orderByEnum
     *            Order by (ASC, DESC)
     * @param criteriaMap
     *            The key-value collections will be transfered to "key=value" in SQL
     * @return The result list of the query with criteria with specify order
     */
    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, Map<String, String> criteriaMap);

    public List<T> findAll(SortByEnum sortByEnum, OrderByEnum orderByEnum, Map<String, String> criteriaMap,
                    boolean isIncludeDeleted);

    /**
     * Get with criteria with pagination
     * 
     * @param currentPage
     *            The page number (start from 0)
     * @param pageSize
     *            The size of each page
     * @param sortByEnum
     *            The sort field
     * @param orderByEnum
     *            Order by (ASC, DESC)
     * @param criteriaMap
     *            The key-value collections will be transfered to "key=value" in SQL
     * @return The result list of the query with criteria with specify order
     */
    public Page<T> findByPage(int currentPage, int pageSize, SortByEnum sortByEnum, OrderByEnum orderByEnum,
                    Map<String, String> criteriaMap);

    public Page<T> findByPage(int currentPage, int pageSize, SortByEnum sortByEnum, OrderByEnum orderByEnum,
                    Map<String, String> criteriaMap, boolean isIncludeDeleted);

    public List<T> findByCriteria(Map<String, String> criteriaMap);

    public List<T> findByCriteria(Map<String, String> criteriaMap, boolean isIncludeDeleted);
}
