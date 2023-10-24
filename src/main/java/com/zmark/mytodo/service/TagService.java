package com.zmark.mytodo.service;

import com.zmark.mytodo.dao.TagDAO;
import com.zmark.mytodo.dto.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class TagService {
    private final TagDAO tagDAO;

    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public TagDTO findTagByName(String tagName) {
        return Optional.ofNullable(tagDAO.findByTagName(tagName))
                .map(Tag::toTagDTO)
                .orElse(null);
    }

    public List<TagDTO> findAllTags() {
        return tagDAO.findAll().stream().map(Tag::toTagDTO).toList();
    }

    /**
     * @param tag tag的名字，可以是多级tag，用/分割
     * @return 创建的子tag
     */
    @Transactional(rollbackFor = NewEntityException.class)
    public void createNewTag(String tag) {
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
    }
}
