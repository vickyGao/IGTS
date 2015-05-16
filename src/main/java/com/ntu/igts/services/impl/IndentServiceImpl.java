package com.ntu.igts.services.impl;

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
import com.ntu.igts.model.Indent;
import com.ntu.igts.repository.IndentRepository;
import com.ntu.igts.services.IndentService;

@Service
public class IndentServiceImpl implements IndentService {

    @Resource
    private IndentRepository indentRepository;

    @Override
    @Transactional
    public Indent create(Indent indent) {
        return indentRepository.create(indent);
    }

    @Override
    @Transactional
    public Indent update(Indent indent) {
        return indentRepository.update(indent);
    }

    @Override
    @Transactional
    public boolean delete(String indentId) {
        indentRepository.delete(indentId);
        Indent indent = indentRepository.findById(indentId);
        if (indent == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Indent> getByUserId(String userId) {
        return indentRepository.getByUserId(userId);
    }

    @Override
    public Indent getById(String indentId) {
        return indentRepository.findById(indentId);
    }

    @Override
    public Page<Indent> getPaginatedIndentByUserId(int currentPage, int pageSize, String userId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_USERID, userId);
        return indentRepository.findByPage(currentPage, pageSize, SortByEnum.CREATED_TIME, OrderByEnum.DESC,
                        criteriaMap);
    }

    @Override
    public Indent getByCommodityId(String commodityId) {
        List<Indent> indentList = indentRepository.getByCommodityId(commodityId);
        if (indentList.size() >= 1) {
            return indentList.get(0);
        } else {
            return null;
        }
    }

}
