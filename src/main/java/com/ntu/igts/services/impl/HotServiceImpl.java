package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.ntu.igts.model.Hot;
import com.ntu.igts.repository.HotRepository;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.HotService;
import com.ntu.igts.services.ImageService;

@Service
public class HotServiceImpl implements HotService {

    @Resource
    private HotRepository hotRepository;
    @Resource
    private CommodityService commodityService;
    @Resource
    private ImageService imageService;

    @Override
    public Hot create(Hot hot) {
        return hotRepository.create(hot);
    }

    @Override
    public Hot update(Hot hot) {
        return hotRepository.update(hot);
    }

    @Override
    public boolean delete(String hotCommodityId) {
        hotRepository.delete(hotCommodityId);
        Hot hot = hotRepository.findById(hotCommodityId);
        if (hot == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Hot getById(String hotId) {
        return hotRepository.findById(hotId);
    }

    @Override
    public List<Hot> getHotCommodities() {
        List<Hot> hotCommodities = hotRepository.findAll();
        for (Hot hot : hotCommodities) {
            hot.setCommodity(commodityService.getCommodityWithDetailById(hot.getCommodityId()));
            hot.setImage(imageService.getById(hot.getImageId()));
        }
        return hotCommodities;
    }

    @Override
    public Hot getDetailById(String hotId) {
        Hot hot = hotRepository.findById(hotId);
        if (hot != null) {
            hot.setCommodity(commodityService.getCommodityWithDetailById(hot.getCommodityId()));
            hot.setImage(imageService.getById(hot.getImageId()));
        }
        return hot;
    }

}
