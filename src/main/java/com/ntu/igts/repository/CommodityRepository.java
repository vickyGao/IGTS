package com.ntu.igts.repository;

import org.springframework.data.jpa.repository.Query;

import com.ntu.igts.model.Commodity;

public interface CommodityRepository extends MyRepository<Commodity, String>, CommodityCustomizeRepository {

    @Query("select count(*) from Commodity t where t.deletedYN='N'")
    public int getTotalCouont();
}
