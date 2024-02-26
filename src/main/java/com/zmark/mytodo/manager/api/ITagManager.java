package com.zmark.mytodo.manager.api;

import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/18 22:28
 */
public interface ITagManager {
    /**
     * @param tagPath tagPath，可以是多级tag，用/分割<br/>
     *                例如：<br/>
     *                1. tag1/tag2/tag3<br/>
     * @throws NewEntityException 如果重名tag存在于其他层级中，则抛出异常
     */
    Tag createNewTag(String tagPath) throws NewEntityException;

    List<Tag> findAll();

    List<Tag> findAllByParentTagIsNull();

    Tag findByTagName(String tagName);

    List<Tag> findTagsByParentTagId(Long id);

    Tag save(Tag tag);

    Tag findTagById(long id);
}
