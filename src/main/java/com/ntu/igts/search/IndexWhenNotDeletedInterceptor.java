package com.ntu.igts.search;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.Commodity;

public class IndexWhenNotDeletedInterceptor implements EntityIndexingInterceptor<Commodity> {

    @Override
    public IndexingOverride onAdd(Commodity entity) {
        if (!Constants.LOGIC_DELETED.equals(entity.getDeletedYN())) {
            return IndexingOverride.APPLY_DEFAULT;
        } else {
            return IndexingOverride.SKIP;
        }
    }

    @Override
    public IndexingOverride onUpdate(Commodity entity) {
        if (Constants.LOGIC_DELETED.equals(entity.getDeletedYN())) {
            return IndexingOverride.REMOVE;
        } else {
            return IndexingOverride.UPDATE;
        }
    }

    @Override
    public IndexingOverride onDelete(Commodity entity) {
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onCollectionUpdate(Commodity entity) {
        return onUpdate(entity);
    }

}
