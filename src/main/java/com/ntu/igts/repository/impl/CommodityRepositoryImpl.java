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

        //通过JPA创建和运行搜索
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                        .forEntity(Commodity.class).get();

        // Set default max value for the price
        if (query.getEndPrice() == 0) {
            query.setEndPrice(Double.MAX_VALUE);
        }

        // Build lucene query for search term and price search
        org.apache.lucene.search.Query luceneQuery = null;
        if (StringUtil.isEmpty(query.getSearchTerm())) {
            luceneQuery = queryBuilder.range().onField("price").from(query.getStartPrice()).to(query.getEndPrice())
                            .createQuery();
        } else {
            luceneQuery = queryBuilder
                            .bool()
                            .must(queryBuilder.keyword().wildcard().onFields("title", "description", "tags.name")
                                            .matching(getWildcardSerchTerm(query.getSearchTerm())).createQuery())
                            .must(queryBuilder.range().onField("price").from(query.getStartPrice())
                                            .to(query.getEndPrice()).createQuery()).createQuery();
        }

        // Set the start page for pagination
        int page = query.getPage() < 0 ? 0 : query.getPage();
        // Transfer lucene query to full text query
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Commodity.class);

        // If the activeYN is not null, add filter for activeYN
        if (query.getActiveYN() != null) {
            fullTextQuery.enableFullTextFilter("activeYnFilter").setParameter("activeYN", query.getActiveYN().value());
        }

        // If the district is not empty, add filter for district
        if (!StringUtil.isEmpty(query.getDistrict())) {
            fullTextQuery.enableFullTextFilter("districtFilter").setParameter("district", query.getDistrict());
        }
        // If the status is not empty, add filter for status
        if (query.getStatus() != null) {
            fullTextQuery.enableFullTextFilter("statusFilter").setParameter("status", query.getStatus().value());
        }
        // If the tag id is not empty, add filter for tag
        if (!StringUtil.isEmpty(query.getTagId())) {
            fullTextQuery.enableFullTextFilter("tagFilter").setParameter("tagId", query.getTagId());
        }

        // Get the total count for the search
        int resultSize = fullTextQuery.getResultSize();
        if (resultSize == 0) {
            return getCommodityQueryResult(query, new ArrayList<Commodity>(), 0, 0);
        } else {
            if (query.getSortBy() == null) {
                fullTextQuery.setFirstResult(page * query.getSize()).setMaxResults(query.getSize());
                List<Commodity> result = fullTextQuery.getResultList();
                return getCommodityQueryResult(query, result, resultSize,
                                getTotalPagesByTotalCountAndPageSize(resultSize, query.getSize()));
            } else {
                Sort sort = getSortForQuery(query);
                fullTextQuery.setSort(sort);
                fullTextQuery.setFirstResult(page * query.getSize()).setMaxResults(query.getSize());
                List<Commodity> result = fullTextQuery.getResultList();
                return getCommodityQueryResult(query, result, resultSize,
                                getTotalPagesByTotalCountAndPageSize(resultSize, query.getSize()));
            }
        }
    }

    /**
     * Get the CommodityQueryResult object
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
    private CommodityQueryResult getCommodityQueryResult(Query query, List<Commodity> items, int totalCount,
                    int totalPages) {
        CommodityQueryResult itemQueryResult = new CommodityQueryResult();
        itemQueryResult.setSearchTerm(query.getSearchTerm());
        itemQueryResult.setCurrentPage(query.getPage() < 0 ? 0 : query.getPage());
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

    /**
     * Get sort for search by query object
     * 
     * @param query
     *            The query object
     * @return org.apache.lucene.search.Sort
     */
    private Sort getSortForQuery(Query query) {
        if (SortByEnum.COLLECTION_NUMBER.equals(query.getSortBy())) {
            return new Sort(new SortField(query.getSortBy().value(), SortField.Type.INT, OrderByEnum.ASC.equals(query
                            .getOrderBy()) ? false : true));
        } else if (SortByEnum.PRICE.equals(query.getSortBy())) {
            return new Sort(new SortField(query.getSortBy().value(), SortField.Type.DOUBLE,
                            OrderByEnum.ASC.equals(query.getOrderBy()) ? false : true));
        } else {
            return new Sort(new SortField(SortByEnum.RELEASE_DATE.value(), SortField.Type.STRING, true));
        }
    }

    /**
     * Get total page number from total count and page size
     * 
     * @param toalCount
     *            The total count of search result
     * @param pageSize
     *            The size of each page
     * @return The number of total pages
     */
    private int getTotalPagesByTotalCountAndPageSize(int toalCount, int pageSize) {
        return (toalCount - 1) / pageSize + 1;
    }

    /**
     * Split the search term into different parts (e.g [A B] -> [*A*B*])
     * 
     * @param originSearchTerm
     *            The search term get from front
     * @return The split search term, with * between each two characters
     */
    private String getWildcardSerchTerm(String originSearchTerm) {
        StringBuffer serchTermStringBuffer = new StringBuffer("*");
        if (originSearchTerm != null) {
            String[] splitSearchTerms = originSearchTerm.split(" ");
            if (splitSearchTerms != null) {
                for (String splitSearchTerm : splitSearchTerms) {
                    if (splitSearchTerm.trim().length() > 0) {
                        serchTermStringBuffer.append(splitSearchTerm + "*");
                    }
                }
            }
        }

        return serchTermStringBuffer.toString();
    }
}
