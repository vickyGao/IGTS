package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;

public interface CommodityService {

    public Commodity create(Commodity commodity);

    public Commodity update(Commodity commodity);

    public boolean delete(String commodityId);

    public Commodity getById(String commodityId);

    public Commodity getCommodityWithDetailById(String commodityId);

    public List<Commodity> getCommodities();

    public Page<Commodity> getByPage(Query query);

    public CommodityQueryResult getCommoditiesBySearchTerm(Query query);

    public int getTotalCount();
}
