package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Cover;

public interface CoverRepository extends MyRepository<Cover, String> {

    @Query("from Cover c where c.commodityId=:commodityId and c.deletedYN='N' order by c.displaySequence ASC")
    public List<Cover> getCoversByCommodityId(@Param("commodityId") String commodityId);
}
