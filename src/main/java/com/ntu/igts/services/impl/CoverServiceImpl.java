package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ntu.igts.model.Cover;
import com.ntu.igts.repository.CoverRepository;
import com.ntu.igts.services.CoverService;
import com.ntu.igts.services.ImageService;

@Service
public class CoverServiceImpl implements CoverService {

    @Resource
    private CoverRepository coverRepository;
    @Resource
    private ImageService imageService;

    @Override
    public Cover create(Cover cover) {
        return coverRepository.create(cover);
    }

    @Override
    public Cover update(Cover cover) {
        return coverRepository.update(cover);
    }

    @Override
    public boolean delete(String coverId) {
        coverRepository.delete(coverId);
        Cover cover = coverRepository.findById(coverId);
        if (cover == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Cover getById(String coverId) {
        return coverRepository.findById(coverId);
    }

    @Override
    public List<Cover> getCoversByCommodityId(String commodityId) {
        List<Cover> covers = coverRepository.getCoversByCommodityId(commodityId);
        for (Cover cover : covers) {
            cover.setImage(imageService.getById(cover.getImageId()));
        }
        return covers;
    }

    @Override
    public int getCurrentMaxDisplaySequenceForCommodity(String commodityId) {
        List<Cover> covers = coverRepository.getCoversByCommodityId(commodityId);
        if (covers.size() > 0) {
            return covers.get(0).getDisplaySequence();
        }
        return 0;
    }

}
