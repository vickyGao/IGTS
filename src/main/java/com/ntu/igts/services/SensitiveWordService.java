package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.model.container.Query;

public interface SensitiveWordService {

    public SensitiveWord create(SensitiveWord sensitiveWord);

    public SensitiveWord update(SensitiveWord sensitiveWord);

    public boolean delete(String sensitiveWordId);

    public SensitiveWord getById(String sensitiveWordId);

    public List<SensitiveWord> getAll();

    public boolean isSensitiveWord(String sensitiveWord);

    public Page<SensitiveWord> getPaginatedSensitiveWord(Query query);
}
