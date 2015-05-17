package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.CommodityTag;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.CommodityRepository;
import com.ntu.igts.repository.CommodityTagRepository;
import com.ntu.igts.services.BillService;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.CoverService;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.services.IndentService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.services.UserService;
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
    @Resource
    private UserService userService;
    @Resource
    private IndentService indentService;
    @Resource
    private BillService billService;

    @Override
    public Commodity create(Commodity commodity) {
        Commodity insertedCommodity = commodityRepository.create(commodity);
        if (insertedCommodity != null) {
            List<Tag> tags = commodity.getTags();
            for (Tag tag : tags) {
                tag = tagService.getById(tag.getId());
                if (tag != null) {
                    CommodityTag commodityTag = new CommodityTag();
                    commodityTag.setCommodityId(insertedCommodity.getId());
                    commodityTag.setTagId(tag.getId());
                    commodityTagRepository.create(commodityTag);
                }
            }
        }
        return getCommodityDetailForCommodity(insertedCommodity);
    }

    @Override
    public Commodity update(Commodity commodity) {
        return commodityRepository.update(commodity);
    }

    @Override
    public boolean delete(String commodityId) {
        List<CommodityTag> commodityTags = commodityTagRepository.getCommodityTagsForCommodityId(commodityId);
        for (CommodityTag commodityTag : commodityTags) {
            commodityTagRepository.delete(commodityTag.getId());
        }
        commodityRepository.delete(commodityId);
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
            commodity.setTags(tagService.getTagsHorizontalByCommodityId(commodity.getId()));
            commodity.setCovers(coverService.getCoversByCommodityId(commodity.getId()));
            return commodity;
        } else {
            return null;
        }
    }

    @Override
    public int getTotalCount() {
        return commodityRepository.getTotalCouont();
    }

    @Override
    public List<Commodity> getAll() {
        return commodityRepository.findAll();
    }

}
