package com.ntu.igts.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

public class CommodityActiveYnFilterFactory {

    private String activeYN;

    public String getActiveYN() {
        return activeYN;
    }

    public void setActiveYN(String activeYN) {
        this.activeYN = activeYN;
    }

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery(new Term("activeYN", activeYN.toLowerCase()));
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }

    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter(activeYN);
        return key;
    }
}
