package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ntu.igts.model.Tag;
import com.ntu.igts.repository.TagRepository;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.StringUtil;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Override
    public Tag create(Tag tag) {
        return tagRepository.create(tag);
    }

    @Override
    public Tag update(Tag tag) {
        return tagRepository.update(tag);
    }

    @Override
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

}
