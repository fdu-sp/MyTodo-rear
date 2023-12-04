package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 15:12
 */
public interface ITagService {
    TagDTO findTagByName(String tagName);

    List<TagDTO> findAllTags();

    /**
     * 根据tag的名字，查找tag的所有子tag，以及子tag的子tag，以此类推
     */
    TagDTO findTagWithAllChildren(String tagName);

    /**
     * @param tagPath tagPath，可以是多级tag，用/分割<br/>
     *                例如：<br/>
     *                1. tag1/tag2/tag3<br/>
     * @throws NewEntityException 如果重名tag存在于其他层级中，则抛出异常
     */
    @Transactional
    Tag createNewTag(String tagPath) throws NewEntityException;
}
