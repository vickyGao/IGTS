package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Hot;

public interface HotService {

    public Hot create(Hot hot);

    public Hot update(Hot hot);

    public boolean delete(String hotCommodityId);

    public Hot getById(String hotId);

    public List<Hot> getHotCommodities();
}
