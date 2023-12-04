package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.service.api.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TagService implements ITagService {
    private final TagDAO tagDAO;

    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public TagDTO findTagByName(String tagName) {
        return Optional.ofNullable(tagDAO.findByTagName(tagName))
                .map(Tag::toTagDTO)
                .orElse(null);
    }

    @Override
    public List<TagDTO> findAllTags() {
        return tagDAO.findAll().stream().map(Tag::toTagDTO).toList();
    }

    @Override
    public TagDTO findTagWithAllChildren(String tagName) {
        Tag tag = tagDAO.findByTagName(tagName);
        if (tag == null) {
            return null;
        }
        List<Tag> childrenTagList = tagDAO.findTagsByParentTagId(tag.getId());
        return tag.toTagDTO(childrenTagList);
    }

    @Override
    @Transactional
    public Tag createNewTag(String tag) throws NewEntityException {
        // 按照/分割tag
        String[] tags = tag.split("/");
        // 从第一个tag开始，如果不存在，则依次创建tag
        // 使用事务进行控制，如果重名tag存在于其他层级中，则抛出异常，回滚事务
        Tag parentTag = null;
        for (String tagName : tags) {
            Tag tagEntity = tagDAO.findByTagName(tagName);
            if (tagEntity == null) {
                tagEntity = Tag.builder()
                        .tagName(tagName)
                        .parentTagId(parentTag == null ? null : parentTag.getId())
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .updateTime(new Timestamp(System.currentTimeMillis()))
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
}
