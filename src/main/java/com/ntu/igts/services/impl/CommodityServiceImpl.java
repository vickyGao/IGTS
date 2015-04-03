package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.CommodityTag;
import com.ntu.igts.model.Cover;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.CommodityRepository;
import com.ntu.igts.repository.CommodityTagRepository;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.CoverService;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.StringUtil;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Resource
    private TagService tagService;
    @Resource
    private CommodityRepository commodityRepository;
    @Resource
    private CommodityTagRepository commodityTagRepository;
    @Resource
    private ImageService imageService;
    @Resource
    private CoverService coverService;

    @Override
    @Transactional
    public Commodity create(Commodity commodity) {
        Commodity insertedCommodity = commodityRepository.create(commodity);
        if (insertedCommodity != null) {
            List<Tag> tags = commodity.getTags();
            for (Tag tag : tags) {
                CommodityTag commodityTag = new CommodityTag();
                commodityTag.setCommodityId(insertedCommodity.getId());
                commodityTag.setTagId(tag.getId());
                commodityTagRepository.create(commodityTag);
            }
        }
        return insertedCommodity;
    }

    @Override
    @Transactional
    public Commodity update(Commodity commodity) {
        return commodityRepository.update(commodity);
    }

    @Override
    @Transactional
    public boolean delete(String commodityId) {
        List<CommodityTag> commodityTags = commodityTagRepository.getCommodityTagsForCommodityId(commodityId);
        for (CommodityTag commodityTag : commodityTags) {
            commodityTagRepository.delete(commodityTag.getId(), false);
        }
        commodityRepository.delete(commodityId, false);
        Commodity commodity = commodityRepository.findById(commodityId);
        if (commodity == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Commodity getById(String commodityId) {
        return commodityRepository.findById(commodityId);

    }

    @Override
    public Commodity getCommodityWithDetailById(String commodityId) {
        Commodity commodity = commodityRepository.findById(commodityId);
        return getCommodityDetailForCommodity(commodity);
    }

    @Override
    public List<Commodity> getCommodities() {
        return commodityRepository.findAll();
    }

    @Override
    public Page<Commodity> getByPage(Query query) {
        if (query.getSearchTerm() == null) {
            query.setSearchTerm(StringUtil.EMPTY);
        }
        if (query.getSortBy() == null) {
            query.setSortBy(SortByEnum.COMMODITY_TITILE);
        }
        if (query.getOrderBy() == null) {
            query.setOrderBy(OrderByEnum.ASC);
        }
        return commodityRepository.findByPage(query);
    }

    @Override
    public CommodityQueryResult getCommoditiesBySearchTerm(Query query) {
        CommodityQueryResult result = commodityRepository.getItemsBySearchTerm(query);
        for (Commodity commodity : result.getContent()) {
            commodity = getCommodityDetailForCommodity(commodity);
        }
        return result;
    }

    private Commodity getCommodityDetailForCommodity(Commodity commodity) {
        if (commodity != null) {
            List<Tag> tags = tagService.getTopLevelTagsForCommodityId(commodity.getId());
            for (Tag tag : tags) {
                tag.setTags(tagService.getTagsWithSubTagsForParentId(tag.getId()));
            }
            commodity.setTags(tags);
            List<Cover> covers = coverService.getCoversByCommodityId(commodity.getId());
            commodity.setCovers(covers);
            return commodity;
        } else {
            return null;
        }
    }

}
