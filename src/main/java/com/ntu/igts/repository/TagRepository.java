package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Tag;

public interface TagRepository extends MyRepository<Tag, String> {

    @Query("from Tag t where t.parentId=:parentId and t.deletedYN='N'")
    List<Tag> getTagsForParentId(@Param("parentId") String parentId);

    @Query("select t from Tag t,CommodityTag e where t.id=e.tagId and e.commodityId=:commodityId and e.deletedYN='N' and t.parentId=null and t.deletedYN='N'")
    List<Tag> getTopLevelTagsForCommodityId(@Param("commodityId") String commodityId);

    @Query("from Tag t where t.standardName=:standardName and t.deletedYN='N'")
    Tag getTagForStandardName(@Param("standardName") String standardName);
}
