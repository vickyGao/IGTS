package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.Tag;
import com.ntu.igts.repository.TagRepository;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.StringUtil;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Override
    @Transactional
    public Tag create(Tag tag) {
        return tagRepository.create(tag);
    }

    @Override
    @Transactional
    public Tag update(Tag tag) {
        return tagRepository.update(tag);
    }

    @Override
    @Transactional
    public boolean delete(String tagId) {
        tagRepository.delete(tagId);
        Tag tag = tagRepository.findById(tagId);
        if (tag == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Tag> getTagsWithSubTagsForParentId(String parentId) {
        List<Tag> tags = tagRepository.getTagsForParentId(parentId);
        for (Tag tag : tags) {
            String id = tag.getId();
            if (!StringUtil.isEmpty(id)) {
                tag.setTags(getTagsWithSubTagsForParentId(id));
            }
        }
        return tags;
    }

    @Override
    public Tag getTopLevelTagWithSubTagsForTagId(String tagId) {
        Tag topLevelTag = tagRepository.findById(tagId);
        if (topLevelTag.getParentId() == null) {
            topLevelTag.setTags(getTagsWithSubTagsForParentId(topLevelTag.getId()));
            return topLevelTag;
        }
        return null;
    }

    @Override
    public List<Tag> getTopLevelTagsForCommodityId(String commodityId) {
        return tagRepository.getTopLevelTagsForCommodityId(commodityId);
    }

    @Override
    public Tag getById(String tagId) {
        return tagRepository.findById(tagId);
    }

    @Override
    public Tag getTagForStandardName(String standardName) {
        return tagRepository.getTagForStandardName(standardName);
    }

    @Override
    public List<Tag> getAllTopLevelTags() {
        return tagRepository.getAllTopLevelTags();
    }

    @Override
    public List<Tag> getAllTagsWithSubTags() {
        List<Tag> topLevelTags = tagRepository.getAllTopLevelTags();
        for (Tag topLevelTag : topLevelTags) {
            List<Tag> subTags = getTagsWithSubTagsForParentId(topLevelTag.getId());
            topLevelTag.setTags(subTags);
        }
        return topLevelTags;
    }

    @Override
    public List<Tag> getTagsByCommodityId(String commodityId) {
        List<Tag> topLevelTags = getTopLevelTagsForCommodityId(commodityId);
        for (Tag topLevelTag : topLevelTags) {
            topLevelTag.setTags(getTagsWithSubTagsForParentId(topLevelTag.getId()));
        }
        return topLevelTags;
    }

    @Override
    public List<Tag> getTagsHorizontalByCommodityId(String commodityId) {
        return tagRepository.getTagsHorizontalByCommodityId(commodityId);
    }

    @Override
    public int getTotalCount() {
        return tagRepository.getTotalCouont();
    }

}
