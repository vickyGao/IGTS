package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Cover;

public interface CoverService {

    public Cover create(Cover cover);

    public Cover update(Cover cover);

    public boolean delete(String coverId);

    public Cover getById(String coverId);

    public List<Cover> getCoversByCommodityId(String commodityId);

    public int getCurrentMaxDisplaySequenceForCommodity(String commodityId);
}
