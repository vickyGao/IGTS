package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Message;

public interface MessageService {

    public Message create(Message message);

    public Message update(Message message);

    public boolean delete(String messageId);

    public List<Message> getByCommodityId(String commodityId);

    public Message getById(String messageId);
}
