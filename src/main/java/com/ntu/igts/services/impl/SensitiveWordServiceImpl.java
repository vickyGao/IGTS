package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.repository.SensitiveWordRepository;
import com.ntu.igts.services.SensitiveWordService;

public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Resource
    private SensitiveWordRepository sensitiveWordRepository;

    @Override
    public SensitiveWord create(SensitiveWord sensitiveWord) {
        return sensitiveWordRepository.create(sensitiveWord);
    }

    @Override
    public SensitiveWord update(SensitiveWord sensitiveWord) {
        return sensitiveWordRepository.update(sensitiveWord);
    }

    @Override
    public SensitiveWord getById(String sensitiveWordId) {
        return sensitiveWordRepository.findById(sensitiveWordId);
    }

    @Override
    public List<SensitiveWord> getAll() {
        return sensitiveWordRepository.findAll();
    }

    @Override
    public boolean isSensitiveWord(String sensitiveWord) {
        // TODO Auto-generated method stub
        return false;
    }

}
