package com.zmark.mytodo;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.service.impl.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ZMark
 * @date 2023/12/4 16:05
 */
@Slf4j
@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService tagService;
    @Autowired
    private TagDAO tagDAO;

    // todo maybe mock tagDAO
    @Test
    void contextLoads() {
    }

    @Test
    void testCreateNewTagNormal() throws NewEntityException {
        tagService.createNewTag("tag1/tag2/tag3");
        tagService.createNewTag("tag1/tag2/tag4");
        tagService.createNewTag("tag1/tag2/tag5");
    }

    @Test
    void testCreateNewTagException() {
        try {
            log.info("尝试创建tag1/tag2/tag3");
            tagService.createNewTag("tag1/tag2/tag3");
            log.info("tag1/tag2/tag3 create succeed");
        } catch (Exception e) {
            log.error("tag1/tag2/tag3 create failed", e);
        }

        try {
            log.info("尝试创建tag1/tag2/tag5");
            tagService.createNewTag("tag1/tag2/tag5");
            log.info("tag1/tag2/tag5 create succeed");
        } catch (Exception e) {
            log.error("tag1/tag2/tag5 create failed", e);
        }

        try {
            log.info("尝试创建tag1/tag3/tag4");
            tagService.createNewTag("tag1/tag3/tag4");
            log.error("tag1/tag3/tag4 created but it shouldn't!!!");
        } catch (Exception e) {
            log.warn("创建tag失败：", e);
        }

        try {
            log.info("尝试创建tag6/tag6/tag90");
            tagService.createNewTag("tag6/tag6/tag90");
            log.error("tag6/tag6/tag90 created but it shouldn't!!!");
        } catch (Exception e) {
            log.warn("创建tag失败：", e);
        }
    }

    @Test
    void testTagToDTO() throws NewEntityException {
        tagService.createNewTag("tag1/tag2/tag3");
        tagService.createNewTag("tag1/tag2/tag4");
        tagService.createNewTag("tag1/tag2/tag5");
        tagDAO.findAll().forEach(tag -> log.info(tag.toTagDTO().toString()));
    }

    @Test
    void testFindAllTagsWithNoData() {
        tagService.findAllTags().forEach(tagDTO -> log.info(tagDTO.toString()));
    }

    @Test
    void testFindAllTags() throws NewEntityException {
        tagService.createNewTag("tag1/tag2/tag3");
        tagService.createNewTag("tag1/tag2/tag4");
        tagService.createNewTag("tag1/tag2/tag5");
        tagService.createNewTag("tag/tag6/tag7");
        tagService.findAllTags().forEach(TagDTO -> log.info(TagDTO.toString()));
    }
}
