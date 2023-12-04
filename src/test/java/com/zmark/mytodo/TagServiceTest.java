package com.zmark.mytodo;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.service.impl.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author ZMark
 * @date 2023/12/4 16:05
 */
@Slf4j
@SpringBootTest
public class TagServiceTest {
    private static Map<String, Tag> existTagMap;

    private TagService tagService;

    @MockBean
    private TagDAO tagDAO;

    @BeforeAll
    static void beforeAll() {
        log.info("TagServiceTest start");
        existTagMap = new HashMap<>();
    }

    @BeforeEach
    void setUp() {
        existTagMap.clear();
        reset(tagDAO);
        tagService = new TagService(tagDAO);

        when(tagDAO.findByTagName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return existTagMap.get(tagName);
        });
        when(tagDAO.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId((long) existTagMap.size());
            existTagMap.put(tag.getTagName(), tag);
            log.info("tag {} saved", tag.getTagName());
            return tag;
        });
    }

    @Test
    void testCreateNewTagNormal() {
        Assertions.assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag3"));
        verify(tagDAO, times(3)).findByTagName(anyString());
        verify(tagDAO, times(3)).save(any(Tag.class));

        Assertions.assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag4"));
        verify(tagDAO, times(6)).findByTagName(anyString());
        verify(tagDAO, times(4)).save(any(Tag.class));

        Assertions.assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag5"));
        verify(tagDAO, times(9)).findByTagName(anyString());
        verify(tagDAO, times(5)).save(any(Tag.class));

        Assertions.assertDoesNotThrow(() -> tagService.createNewTag("tag/tag6/tag7"));
        verify(tagDAO, times(12)).findByTagName(anyString());
        verify(tagDAO, times(8)).save(any(Tag.class));
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
        tagDAO.findAll().forEach(tag -> log.info(TagDTO.from(tag).toString()));
    }

    @Test
    void testFindAllTagsWithNoData() {
        tagService.findAllTagsWithAllChildren().forEach(tagDTO -> log.info(tagDTO.toString()));
    }

    @Test
    void testFindAllTags() throws NewEntityException {
        tagService.createNewTag("tag1/tag2/tag3");
        tagService.createNewTag("tag1/tag2/tag4");
        tagService.createNewTag("tag1/tag2/tag5");
        tagService.createNewTag("tag/tag6/tag7");
        tagService.findAllTagsWithAllChildren().forEach(TagDTO -> log.info(TagDTO.toString()));
    }
}
