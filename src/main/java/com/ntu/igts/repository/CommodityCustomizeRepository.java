package com.ntu.igts.repository;

import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;

public interface CommodityCustomizeRepository {

    public CommodityQueryResult getItemsBySearchTerm(Query query);
}
