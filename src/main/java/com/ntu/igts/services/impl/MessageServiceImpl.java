package com.ntu.igts.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Message;
import com.ntu.igts.repository.MessageRepository;
import com.ntu.igts.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageRepository messageRepository;

    @Override
    @Transactional
    public Message create(Message message) {
        Date currentDate = new Date();
        message.setMessageTime(currentDate);
        return messageRepository.create(message);
    }

    @Override
    @Transactional
    public Message update(Message message) {
        return messageRepository.update(message);
    }

    @Override
    @Transactional
    public boolean delete(String messageId) {
        messageRepository.delete(messageId);
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Message> getByCommodityId(String commodityId) {
        List<Message> messages = messageRepository.getParentMessagesByCommodityId(commodityId);
        for (Message message : messages) {
            List<Message> childrenMessages = messageRepository.getChildrenMessagesByParentId(message.getId());
            message.setMessages(childrenMessages);
        }
        return messages;
    }

    @Override
    public Message getById(String messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public Page<Message> getPaginatedMessagesByCommodity(int currentPage, int pageSize, String commodityId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_COMMODITYID, commodityId);
        criteriaMap.put(Constants.FIELD_PARENTID, null);
        Page<Message> topMessagesPage = messageRepository.findByPage(currentPage, pageSize, SortByEnum.CREATED_TIME,
                        OrderByEnum.DESC, criteriaMap);
        if (topMessagesPage != null) {
            for (Message topMessage : topMessagesPage.getContent()) {
                List<Message> childrenMessages = messageRepository.getChildrenMessagesByParentId(topMessage.getId());
                topMessage.setMessages(childrenMessages);
            }
        }
        return topMessagesPage;
    }

}
