package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Message;

public interface MessageRepository extends MyRepository<Message, String> {

    @Query("from Message m where m.parentId=null and m.commodityId=:commodityId and m.deletedYN='N'")
    public List<Message> getParentMessagesByCommodityId(@Param("commodityId") String commodityId);

    @Query("from Message m where m.parentId=:parentId and m.deletedYN='N'")
    public List<Message> getChildrenMessagesByParentId(@Param("parentId") String parentId);
}
