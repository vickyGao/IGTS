package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.SensitiveWord;

public interface SensitiveWordService {

    public SensitiveWord create(SensitiveWord sensitiveWord);

    public SensitiveWord update(SensitiveWord sensitiveWord);

    public SensitiveWord getById(String sensitiveWordId);

    public List<SensitiveWord> getAll();

    public boolean isSensitiveWord(String sensitiveWord);
}
