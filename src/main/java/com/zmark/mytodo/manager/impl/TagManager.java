package com.zmark.mytodo.manager.impl;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.manager.api.ITagManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/18 22:29
 */
@Component
public class TagManager implements ITagManager {
    private final TagDAO tagDAO;

    @Autowired
    public TagManager(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public Tag createNewTag(String tagPath) throws NewEntityException {
        // 按照/分割tag
        String[] tags = tagPath.split("/");
        // 从第一个tag开始，如果不存在，则依次创建tag
        // 如果重名tag存在于其他层级中，则抛出异常，回滚事务
        Tag parentTag = null;
        for (String tagName : tags) {
            Tag tagEntity = tagDAO.findByTagName(tagName);
            if (tagEntity == null) {
                tagEntity = Tag.builder()
                        .tagName(tagName)
                        .parentTag(parentTag)
                        .parentTagId(parentTag == null ? null : parentTag.getId())
                        .build();
                tagDAO.save(tagEntity);
            } else {
                if (parentTag != null && !parentTag.getId().equals(tagEntity.getParentTagId())) {
                    throw new NewEntityException("创建tag失败，tag#" + tagEntity.getTagName() + "已存在于其他层级中");
                }
            }
            parentTag = tagEntity;
        }
        // 返回最后一个tag
        return parentTag;
    }

    @Override
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Override
    public List<Tag> findAllByParentTagIsNull() {
        return tagDAO.findAllByParentTagIsNull();
    }

    @Override
    public Tag findByTagName(String tagName) {
        return tagDAO.findByTagName(tagName);
    }

    @Override
    public List<Tag> findTagsByParentTagId(Long id) {
        return tagDAO.findTagsByParentTagId(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagDAO.save(tag);
    }

    @Override
    public Tag findTagById(long id) {
        return tagDAO.findById(id).orElse(null);
    }
}
