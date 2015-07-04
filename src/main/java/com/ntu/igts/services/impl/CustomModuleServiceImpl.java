package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.CustomModule;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.CustomModuleRepository;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.CustomModuleService;

@Service
public class CustomModuleServiceImpl implements CustomModuleService {

    @Resource
    private CustomModuleRepository customModuleRepository;
    @Resource
    private CommodityService commodityService;

    @Override
    public CustomModule create(CustomModule customModule) {
        return customModuleRepository.create(customModule);
    }

    @Override
    public CustomModule update(CustomModule customModule) {
        return customModuleRepository.update(customModule);
    }

    @Override
    public boolean delete(String customModuleId) {
        customModuleRepository.delete(customModuleId);
        CustomModule customModule = customModuleRepository.findById(customModuleId);
        if (customModule == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public CustomModule getById(String customModuleId) {
        return customModuleRepository.findById(customModuleId);
    }

    @Override
    public List<CustomModule> getCustomModules() {
        return customModuleRepository.findAll(SortByEnum.DISPLAY_SEQUENCE, OrderByEnum.ASC);
    }

    @Override
    public List<CustomModule> getCustomModulesWithDetail() {
        List<CustomModule> customModules = customModuleRepository.findAll(SortByEnum.DISPLAY_SEQUENCE, OrderByEnum.ASC);
        for (CustomModule customModule : customModules) {
            Query query = new Query();
//            query.setActiveYN(ActiveStateEnum.ACTIVE);
//            query.setDistrict("");
            query.setSearchTerm(customModule.getKeyword());
            query.setSortBy(null);
            query.setOrderBy(OrderByEnum.DESC);
            query.setPage(0);
//            query.setTagId("");
//            query.setSortBy(SortByEnum.RELEASE_DATE);
            query.setSize(customModule.getDisplayAmount());
            CommodityQueryResult result = commodityService.getCommoditiesBySearchTerm(query);
            List<Commodity> commodities = result.getContent();
            customModule.setCommodities(commodities);
        }

        return customModules;
    }

}
