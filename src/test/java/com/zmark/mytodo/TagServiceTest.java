package com.zmark.mytodo;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dao.TaskTagMatchDAO;
import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.entity.TaskTagMatch;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.service.impl.TagService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * @author ZMark
 * @date 2023/12/4 16:05
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class TagServiceTest {
    /**
     * Monica数据库中的数据
     */
    private static Map<String, Tag> existTagInDB;
    /**
     * key: TaskTagMatchId<br/>
     * value: TaskTagMatch
     */
    private static Map<Long, TaskTagMatch> existTaskTagMatchInDB;

    @MockBean
    private TagDAO tagDAO;
    @MockBean
    private TaskTagMatchDAO taskTagMatchDAO;

    /**
     * 被测对象
     */
    private TagService tagService;

    @BeforeAll
    static void init() {
        // 初始化模拟数据
        existTagInDB = new HashMap<>();
        existTaskTagMatchInDB = new HashMap<>();
    }

    @BeforeEach
    void setUp() {
        // 清空模拟数据
        existTagInDB.clear();
        existTaskTagMatchInDB.clear();
        // 重置模拟对象
        reset(tagDAO, taskTagMatchDAO);
        // 初始化被测对象
        tagService = new TagService(tagDAO, taskTagMatchDAO);
        // 设置模拟对象的行为
        // mock TagDAO
        when(tagDAO.findByTagName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return existTagInDB.get(tagName);
        });
        when(tagDAO.save(any(Tag.class))).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId((long) existTagInDB.size());
            existTagInDB.put(tag.getTagName(), tag);
            log.info("tag {} saved", tag.getTagName());
            return tag;
        });
        when(tagDAO.findAll()).thenAnswer(invocation -> {
            log.info("findAll invoked");
            return existTagInDB.values().stream().toList();
        });
        when(tagDAO.findTagsByParentTagId(anyLong())).thenAnswer(invocation -> {
            Long parentTagId = invocation.getArgument(0);
            log.info("findTagsByParentTagId invoked, parentTagId: {}", parentTagId);
            return existTagInDB.values().stream()
                    .filter(tag -> tag.getParentTagId() != null && tag.getParentTagId().equals(parentTagId))
                    .toList();
        });
        when(tagDAO.findAllByParentTagIsNull()).thenAnswer(invocation -> {
            log.info("findAllByParentTagIsNull invoked");
            return existTagInDB.values().stream()
                    .filter(tag -> tag.getParentTagId() == null)
                    .toList();
        });
        when(tagDAO.findTagById(anyLong())).thenAnswer(invocation -> {
            Long tagId = invocation.getArgument(0);
            log.info("findTagById invoked, tagId: {}", tagId);
            return existTagInDB.values().stream()
                    .filter(tag -> tag.getId().equals(tagId))
                    .findFirst()
                    .orElse(null);
        });
        doAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            existTagInDB.remove(tag.getTagName());
            log.info("tag {} deleted", tag.getTagName());
            return null; // 对于void方法，这里的null将被忽略
        }).when(tagDAO).delete(any(Tag.class));
        // mock TaskTagMatchDAO
        doAnswer(invocation -> {
            Long tagId = invocation.getArgument(0);
            log.info("deleteAllByTagId invoked, tagId: {}", tagId);
            existTaskTagMatchInDB.values().removeIf(taskTagMatch -> taskTagMatch.getTagId().equals(tagId));
            return null; // 对于void方法，这里的null将被忽略
        }).when(taskTagMatchDAO).deleteAllByTagId(anyLong());
        when(taskTagMatchDAO.save(any(TaskTagMatch.class))).thenAnswer(invocationOnMock -> {
            TaskTagMatch entity = invocationOnMock.getArgument(0);
            if (entity.getId() == null) {
                entity.setId((long) existTaskTagMatchInDB.size());
            }
            existTaskTagMatchInDB.put(entity.getId(), entity);
            log.info("taskTagMatch {} saved", entity.getId());
            return entity;
        });
        when(taskTagMatchDAO.findAll()).thenAnswer(invocation -> {
            log.info("findAll invoked");
            return existTaskTagMatchInDB.values().stream().toList();
        });
    }

    @Test
    void testCreateNewTagNormal() {
        assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag3"));
        verify(tagDAO, times(3)).findByTagName(anyString());
        verify(tagDAO, times(3)).save(any(Tag.class));

        assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag4"));
        verify(tagDAO, times(6)).findByTagName(anyString());
        verify(tagDAO, times(4)).save(any(Tag.class));

        assertDoesNotThrow(() -> tagService.createNewTag("tag1/tag2/tag5"));
        verify(tagDAO, times(9)).findByTagName(anyString());
        verify(tagDAO, times(5)).save(any(Tag.class));

        assertDoesNotThrow(() -> tagService.createNewTag("tag/tag6/tag7"));
        verify(tagDAO, times(12)).findByTagName(anyString());
        verify(tagDAO, times(8)).save(any(Tag.class));

        Assertions.assertEquals(8, existTagInDB.size());
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
        List<Tag> tagList = tagDAO.findAll();
        Assertions.assertEquals(5, tagList.size());
        Assertions.assertEquals(1, tagList.stream().filter(tag -> tag.getTagName().equals("tag1")).count());
        Assertions.assertEquals(1, tagList.stream().filter(tag -> tag.getTagName().equals("tag2")).count());
        Assertions.assertEquals(1, tagList.stream().filter(tag -> tag.getTagName().equals("tag3")).count());
        Assertions.assertEquals(1, tagList.stream().filter(tag -> tag.getTagName().equals("tag4")).count());
        Assertions.assertEquals(1, tagList.stream().filter(tag -> tag.getTagName().equals("tag5")).count());
        tagList.forEach(tag -> log.info(TagDTO.from(tag).toString()));
    }

    @Test
    void testFindAllTagsWithNoData() {
        List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();
        Assertions.assertEquals(0, tagDTOList.size());
    }

    @Test
    void testFindAllTagsWithAllChildren() throws NewEntityException {
        // 准备数据：
        tagService.createNewTag("大学/课程/智能移动平台开发");
        tagService.createNewTag("大学/课程/软件设计");
        tagService.createNewTag("大学/课程/数据库设计（H）");
        tagService.createNewTag("大学/课程/操作系统（H）");
        tagService.createNewTag("大学/课程/操作系统（H）/操作系统实验");

        // 测试：
        List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();

        // 断言：
        Assertions.assertEquals(7, tagDTOList.size());
        tagDTOList.stream().filter(tagDTO -> tagDTO.getTagName().equals("大学")).findFirst().ifPresent(tagDTO -> {
            Assertions.assertEquals(1, tagDTO.getChildren().size());
            Assertions.assertEquals("大学", tagDTO.getTagPath());
            tagDTO.getChildren().stream().filter(tagDTO1 -> tagDTO1.getTagName().equals("课程")).findFirst().ifPresent(tagDTO1 -> {
                Assertions.assertEquals(4, tagDTO1.getChildren().size());
                Assertions.assertEquals("大学/课程", tagDTO1.getTagPath());
                tagDTO1.getChildren().stream().filter(tagDTO2 -> tagDTO2.getTagName().equals("操作系统（H）")).findFirst().ifPresent(tagDTO2 -> {
                    Assertions.assertEquals(1, tagDTO2.getChildren().size());
                    Assertions.assertEquals("大学/课程/操作系统（H）", tagDTO2.getTagPath());
                    tagDTO2.getChildren().stream().filter(tagDTO3 -> tagDTO3.getTagName().equals("操作系统实验")).findFirst().ifPresent(tagDTO3 -> {
                        Assertions.assertEquals(0, tagDTO3.getChildren().size());
                        Assertions.assertEquals("大学/课程/操作系统（H）/操作系统实验", tagDTO3.getTagPath());
                    });
                });
            });
        });
        tagDTOList.stream().filter(tagDTO -> tagDTO.getTagName().equals("智能移动平台开发")).findFirst().ifPresent(tagDTO -> {
            Assertions.assertEquals(0, tagDTO.getChildren().size());
            Assertions.assertEquals("大学/课程/智能移动平台开发", tagDTO.getTagPath());
        });
        tagDTOList.stream().filter(tagDTO -> tagDTO.getTagName().equals("软件设计")).findFirst().ifPresent(tagDTO -> {
            Assertions.assertEquals(0, tagDTO.getChildren().size());
            Assertions.assertEquals("大学/课程/软件设计", tagDTO.getTagPath());
        });
        tagDTOList.stream().filter(tagDTO -> tagDTO.getTagName().equals("数据库设计（H）")).findFirst().ifPresent(tagDTO -> {
            Assertions.assertEquals(0, tagDTO.getChildren().size());
            Assertions.assertEquals("大学/课程/数据库设计（H）", tagDTO.getTagPath());
        });

        // 打印：
        tagDTOList.forEach(TagDTO -> log.info(TagDTO.toString()));
    }

    @Test
    public void testFindTagWithAllChildren() throws NewEntityException {
        // 准备数据：
        tagService.createNewTag("大学/课程/智能移动平台开发");
        tagService.createNewTag("大学/课程/软件设计");

        // 测试：
        TagDTO universityTag = tagService.findTagWithAllChildren("大学");
        Assertions.assertEquals(1, universityTag.getChildren().size());
        Assertions.assertEquals("大学", universityTag.getTagName());
        Assertions.assertEquals("大学", universityTag.getTagPath());

        TagDTO courseTag = universityTag.getChildren().get(0);
        Assertions.assertEquals(2, courseTag.getChildren().size());
        Assertions.assertEquals("课程", courseTag.getTagName());
        Assertions.assertEquals("大学/课程", courseTag.getTagPath());

        TagDTO intelligentMobilePlatformDevelopmentTag = courseTag.getChildren().get(0);
        Assertions.assertEquals(0, intelligentMobilePlatformDevelopmentTag.getChildren().size());
        Assertions.assertEquals("智能移动平台开发", intelligentMobilePlatformDevelopmentTag.getTagName());
        Assertions.assertEquals("大学/课程/智能移动平台开发", intelligentMobilePlatformDevelopmentTag.getTagPath());

        TagDTO softwareDesignTag = courseTag.getChildren().get(1);
        Assertions.assertEquals(0, softwareDesignTag.getChildren().size());
        Assertions.assertEquals("软件设计", softwareDesignTag.getTagName());
        Assertions.assertEquals("大学/课程/软件设计", softwareDesignTag.getTagPath());

        TagDTO softwareDesignTag2 = tagService.findTagWithAllChildren("软件设计");
        Assertions.assertEquals(0, softwareDesignTag2.getChildren().size());
        Assertions.assertEquals("软件设计", softwareDesignTag2.getTagName());
        Assertions.assertEquals("大学/课程/软件设计", softwareDesignTag2.getTagPath());

        // 打印：
        log.info(universityTag.toString());
        log.info(courseTag.toString());
        log.info(intelligentMobilePlatformDevelopmentTag.toString());
        log.info(softwareDesignTag.toString());
        log.info(softwareDesignTag2.toString());
    }

    @Test
    public void testFindFirstLevelTagsWithAllChildren() throws NewEntityException {
        List<TagDTO> firstLevelTagsWithAllChildren = tagService.findFirstLevelTagsWithAllChildren();
        Assertions.assertEquals(0, firstLevelTagsWithAllChildren.size());

        // 准备数据：
        tagService.createNewTag("大学/课程/智能移动平台开发");
        tagService.createNewTag("大学/课程/软件设计");
        tagService.createNewTag("大学/课程/数据库设计（H）");
        tagService.createNewTag("大学/课程/操作系统（H）");
        tagService.createNewTag("购物/电子产品");
        tagService.createNewTag("购物/服装");
        tagService.createNewTag("工作");

        // 测试：
        firstLevelTagsWithAllChildren = tagService.findFirstLevelTagsWithAllChildren();
        Assertions.assertEquals(3, firstLevelTagsWithAllChildren.size());
    }

    /**
     * @see TagService#deleteTagByName(String)
     */
    @Test
    public void test_deleteTag() throws NewEntityException {
        // 准备数据：
        Tag tag1 = tagService.createNewTag("大学");
        Tag tagA = tagService.createNewTag("大学/课程/智能移动平台开发");
        Tag tagB = tagService.createNewTag("大学/课程/软件设计");
        Tag tagC = tagService.createNewTag("大学/课程/数据库设计（H）");
        Tag tagD = tagService.createNewTag("大学/课程/操作系统（H）");
        Tag tagE = tagService.createNewTag("大学/课程/操作系统（H）/操作系统实验");
        Tag tag2 = tagService.createNewTag("大学2");
        Tag tagF = tagService.createNewTag("大学2/课程sdf");

        taskTagMatchDAO.save(TaskTagMatch.builder().tagId(tag1.getId()).taskId(1L).build());
        taskTagMatchDAO.save(TaskTagMatch.builder().tagId(tagA.getId()).taskId(2L).build());
        taskTagMatchDAO.save(TaskTagMatch.builder().tagId(tag2.getId()).taskId(2L).build());
        taskTagMatchDAO.save(TaskTagMatch.builder().tagId(tagF.getId()).taskId(2L).build());
        assertEquals("测试前tagInDB数量不正确", 9, existTagInDB.size());
        assertEquals("测试前matchInDB数量不正确", 4, existTaskTagMatchInDB.size());

        // 测试：
        // 删除一个tag，但不会删除match和子tag
        assertDoesNotThrow(() -> tagService.deleteTagByName(tagB.getTagName()));
        List<Tag> tagList = tagDAO.findAll();
        assertEquals("删除了tag-<" + tagB.getTagName() + ">后tag总数量不正确", 8, tagList.size());
        List<TaskTagMatch> taskTagMatchList = taskTagMatchDAO.findAll();
        assertEquals("删除了tag-<" + tagB.getTagName() + ">后match总数量不正确", 4, taskTagMatchList.size());

        // 删除一个tag，会删除match和子tag
        assertDoesNotThrow(() -> tagService.deleteTagByName(tag1.getTagName()));
        tagList = tagDAO.findAll();
        assertEquals("删除了tag-<" + tag1.getTagName() + ">后tag总数量不正确", 2, tagList.size());
        taskTagMatchList = taskTagMatchDAO.findAll();
        assertEquals("删除了tag-<" + tagB.getTagName() + ">后match总数量不正确", 2, taskTagMatchList.size());

        // 删除一个末端的tag，会删除match，不会删除子tag（因为没有子tag）
        assertDoesNotThrow(() -> tagService.deleteTagByName(tagF.getTagName()));
        tagList = tagDAO.findAll();
        assertEquals("删除了tag-<" + tagF.getTagName() + ">后tag总数量不正确", 1, tagList.size());
        taskTagMatchList = taskTagMatchDAO.findAll();
        assertEquals("删除了tag-<" + tagB.getTagName() + ">后match总数量不正确", 1, taskTagMatchList.size());

        // 删除最后的tag
        assertDoesNotThrow(() -> tagService.deleteTagByName(tag2.getTagName()));
        tagList = tagDAO.findAll();
        assertEquals("删除了tag-<" + tag2.getTagName() + ">后tag总数量不正确", 0, tagList.size());
        taskTagMatchList = taskTagMatchDAO.findAll();
        assertEquals("删除了tag-<" + tag2.getTagName() + ">后match总数量不正确", 0, taskTagMatchList.size());
    }
}
