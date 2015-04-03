package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.SensitiveWord;

public interface SensitiveWordRepository extends MyRepository<SensitiveWord, String> {

    @Query("from SensitiveWord s where s.word=:word and s.activeYN='Y' and s.deletedYN='N'")
    public List<SensitiveWord> getByWord(@Param("word") String word);

    @Query("from SensitiveWord s where s.activeYN='Y' and s.deletedYN='N'")
    public List<SensitiveWord> getAllActiveSensitiveWords();
}
