package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.test.Order;

public class SensitiveWordServiceTest extends TestBase {

    @Resource
    private SensitiveWordService sensitiveWordService;
    private static SensitiveWord sensitiveWord;

    @Test
    @Order(0)
    public void testCreate() {
        SensitiveWord testSensitiveWord = new SensitiveWord();
        testSensitiveWord.setWord("testSensitiveWord");
        SensitiveWord insertedSensitiveWord = sensitiveWordService.create(testSensitiveWord);
        assertNotNull("Create sensitive word failed", insertedSensitiveWord);

        sensitiveWord = insertedSensitiveWord;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        sensitiveWord.setWord("testSensitiveWord-update");
        SensitiveWord updatedSensitiveWord = sensitiveWordService.update(sensitiveWord);
        assertNotNull("Update sensitive word failed", updatedSensitiveWord);
    }

    @Test
    @Order(20)
    public void testGetById() {
        SensitiveWord returnSensitiveWord = sensitiveWordService.getById(sensitiveWord.getId());
        assertNotNull("Get sensitive word failed", returnSensitiveWord);
    }

    @Test
    @Order(30)
    public void testGetAll() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.getAll();
        assertNotNull("Get all active sensitive words failed", sensitiveWords);
        assertTrue("Get all active sensitive words failed", sensitiveWords.size() > 0);
    }

    @Test
    @Order(40)
    public void testIsSensitiveWord() {
        boolean flag = sensitiveWordService.isSensitiveWord(sensitiveWord.getWord());
        assertTrue("Check whether is sensitive word failed", flag);
    }

    @Test
    @Order(41)
    public void testGetPaginatedSensitiveWord() {
        Query query = new Query();
        query.setSearchTerm(sensitiveWord.getWord());
        query.setPage(0);
        query.setSize(5);
        Page<SensitiveWord> page = sensitiveWordService.getPaginatedSensitiveWord(query);
        assertNotNull("Get paginated sensitive words failed", page);
        assertTrue("Get paginated sensitive words failed", page.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testDelete() {
        boolean flag = sensitiveWordService.delete(sensitiveWord.getId());
        assertTrue("Delete sensitive word failed", flag);
    }
}
