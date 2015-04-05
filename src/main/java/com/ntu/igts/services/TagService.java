package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Tag;

public interface TagService {

    public Tag create(Tag tag);

    public Tag update(Tag tag);

    public boolean delete(String tagId);

    public List<Tag> getTagsWithSubTagsForParentId(String parentId);

    public Tag getTopLevelTagWithSubTagsForTagId(String tagId);

    public List<Tag> getTopLevelTagsForCommodityId(String commodityId);

    public Tag getById(String tagId);

    public Tag getTagForStandardName(String standardName);

    public List<Tag> getAllTopLevelTags();

    public List<Tag> getAllTagsWithSubTags();

    public List<Tag> getTagsByCommodityId(String commodityId);

    public List<Tag> getTagsHorizontalByCommodityId(String commodityId);
}
