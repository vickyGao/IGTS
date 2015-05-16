package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.SensitiveWordRepository;
import com.ntu.igts.services.SensitiveWordService;
import com.ntu.igts.utils.StringUtil;

@Service
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
    public boolean delete(String sensitiveWordId) {
        sensitiveWordRepository.delete(sensitiveWordId);
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(sensitiveWordId);
        if (sensitiveWord == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SensitiveWord getById(String sensitiveWordId) {
        return sensitiveWordRepository.findById(sensitiveWordId);
    }

    @Override
    public List<SensitiveWord> getAll() {
        return sensitiveWordRepository.getAllActiveSensitiveWords();
    }

    @Override
    public boolean isSensitiveWord(String sensitiveWord) {
        List<SensitiveWord> sensitiveWords = sensitiveWordRepository.getByWord(sensitiveWord);
        if (sensitiveWords.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Page<SensitiveWord> getPaginatedSensitiveWord(Query query) {
        if (StringUtil.isEmpty(query.getSearchTerm())) {
            query.setSearchTerm(StringUtil.EMPTY);
        }
        query.setSortBy(SortByEnum.WORD);
        query.setOrderBy(OrderByEnum.ASC);
        return sensitiveWordRepository.findByPage(query);
    }

}
