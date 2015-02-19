package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.CommodityTag;

public interface CommodityTagRepository extends MyRepository<CommodityTag, String> {

    @Query("from CommodityTag c where c.commodityId = :commodityId and c.deletedYN = 'N'")
    public List<CommodityTag> getCommodityTagsForCommodityId(@Param("commodityId") String commodityId);
}
