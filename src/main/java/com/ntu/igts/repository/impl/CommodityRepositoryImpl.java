package com.ntu.igts.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.CommodityCustomizeRepository;
import com.ntu.igts.utils.StringUtil;

public class CommodityRepositoryImpl implements CommodityCustomizeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public CommodityQueryResult getItemsBySearchTerm(Query query) {

        if (query == null) {
            return null;
        }
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                        .forEntity(Commodity.class).get();

        if (query.getEndPrice() == 0) {
            query.setEndPrice(Double.MAX_VALUE);
        }
        org.apache.lucene.search.Query luceneQuery = null;
        if (StringUtil.isEmpty(query.getDistrict())) {
            luceneQuery = queryBuilder
                            .bool()
                            .must(queryBuilder.range().onField("price").from(query.getStartPrice())
                                            .to(query.getEndPrice()).createQuery())
                            .must(queryBuilder.keyword().onField(Constants.FIELD_DELETED_YN).matching("N")
                                            .createQuery())
                            .must(queryBuilder.keyword().fuzzy().onFields("title", "description")
                                            .matching(query.getSearchTerm()).createQuery()).createQuery();
        } else {
            luceneQuery = queryBuilder
                            .bool()
                            .must(queryBuilder.range().onField("price").from(query.getStartPrice())
                                            .to(query.getEndPrice()).createQuery())
                            .must(queryBuilder.keyword().onField("district").matching(query.getDistrict())
                                            .createQuery())
                            .must(queryBuilder.keyword().onField(Constants.FIELD_DELETED_YN).matching("N")
                                            .createQuery())
                            .must(queryBuilder.keyword().fuzzy().onFields("title", "description")
                                            .matching(query.getSearchTerm()).createQuery()).createQuery();
        }
        ;

        int page = query.getPage() > 0 ? query.getPage() - 1 : 0;
        // wrap Lucene query in a javax.persistence.Query
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(luceneQuery,
                        Commodity.class);
        int resultSize = ((FullTextQuery) persistenceQuery).getResultSize();
        if (resultSize == 0) {
            return getItemQueryResult(query, new ArrayList<Commodity>(), 0, 0);
        } else {
            if (query.getSortBy() == null) {
                persistenceQuery.setFirstResult(page * query.getSize()).setMaxResults(query.getSize());
                List<Commodity> result = persistenceQuery.getResultList();
                return getItemQueryResult(query, result, resultSize, resultSize / query.getSize() + 1);
            } else {
                Sort sort = getSortForQuery(query);
                ((FullTextQuery) persistenceQuery).setSort(sort);
                persistenceQuery.setFirstResult(page * query.getSize()).setMaxResults(query.getSize());
                List<Commodity> result = persistenceQuery.getResultList();
                return getItemQueryResult(query, result, resultSize, resultSize / query.getSize() + 1);
            }
        }
    }

    /**
     * Get the ItemQueryResult object
     * 
     * @param query
     *            The query object, including the parameters which are used for query
     * @param items
     *            The search result list
     * @param totalCount
     *            The total count of the search result
     * @param totalPages
     *            The total page number of the pagination result
     * @return The ItemQueryResult object
     */
    private CommodityQueryResult getItemQueryResult(Query query, List<Commodity> items, int totalCount, int totalPages) {
        CommodityQueryResult itemQueryResult = new CommodityQueryResult();
        itemQueryResult.setSearchTerm(query.getSearchTerm());
        itemQueryResult.setCurrentPage(query.getPage() > 0 ? query.getPage() - 1 : 0);
        itemQueryResult.setTotalCount(totalCount);
        itemQueryResult.setTotalPages(totalPages);
        itemQueryResult.setSortBy(query.getSortBy());
        itemQueryResult.setOrderBy(query.getOrderBy());
        itemQueryResult.setStartPrice(query.getStartPrice());
        itemQueryResult.setEndPrice(query.getEndPrice());
        itemQueryResult.setDistrict(query.getDistrict());
        itemQueryResult.setContent(items);
        return itemQueryResult;
    }

    private Sort getSortForQuery(Query query) {
        if (SortByEnum.COLLECTION_NUMBER.equals(query.getSortBy())) {
            return new Sort(new SortField(query.getSortBy().value(), SortField.Type.INT, OrderByEnum.ASC.equals(query
                            .getOrderBy()) ? false : true));
        } else if (SortByEnum.PRICE.equals(query.getSortBy())) {
            return new Sort(new SortField(query.getSortBy().value(), SortField.Type.DOUBLE,
                            OrderByEnum.ASC.equals(query.getOrderBy()) ? false : true));
        } else {
            return new Sort(new SortField(query.getSortBy().value(), SortField.Type.STRING,
                            OrderByEnum.ASC.equals(query.getOrderBy()) ? false : true));
        }
    }

}
