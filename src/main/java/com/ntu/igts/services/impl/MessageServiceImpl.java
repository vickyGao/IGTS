package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
