package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Message;

public interface MessageService {

    public Message create(Message message);

    public Message update(Message message);

    public boolean delete(String messageId);

    public List<Message> getByCommodityId(String commodityId);

    public Message getById(String messageId);

    public Page<Message> getPaginatedMessagesByCommodity(int currentPage, int pageSize, String commodityId);
}
