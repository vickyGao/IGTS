package com.ntu.igts.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

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
     *            Sort by (capital letter of the field in model)
     * @param orderBy
     *            Order by (ASC, DESC)
     * @return The Page object
     */
    public Page<T> findByPage(Query query);

    public Page<T> findByPage(Query query, boolean isIncludeDeleted);
}
