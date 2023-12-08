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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagService implements ITagService {
    private final TagDAO tagDAO;

    @Autowired
    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public List<TagDTO> findAllTagsWithAllChildren() {
        List<Tag> tagList = tagDAO.findAll();
        return getTagDTOListWithAllChildren(tagList);
    }

    @Override
    public List<TagDTO> findFirstLevelTagsWithAllChildren() {
        List<Tag> tagList = tagDAO.findAllByParentTagIsNull();
        return getTagDTOListWithAllChildren(tagList);
    }

    private List<TagDTO> getTagDTOListWithAllChildren(List<Tag> tagList) {
        List<TagDTO> tagDTOList = new ArrayList<>();
        for (Tag tag : tagList) {
            TagDTO tagDTO = this.findTagWithAllChildren(tag.getTagName());
            if (tagDTO != null) {
                tagDTOList.add(tagDTO);
            }
        }
        return tagDTOList;
    }

    @Override
    public TagDTO findTagWithAllChildren(String tagName) {
        Tag tag = tagDAO.findByTagName(tagName);
        if (tag == null) {
            return null;
        }
        List<Tag> childrenTagList = tagDAO.findTagsByParentTagId(tag.getId());
        List<TagDTO> childrenTagDTOList = new ArrayList<>();
        for (Tag childTag : childrenTagList) {
            TagDTO childTagDTO = this.findTagWithAllChildren(childTag.getTagName());
            if (childTagDTO != null) {
                childrenTagDTOList.add(childTagDTO);
            }
        }
        return TagDTO.from(tag, childrenTagDTOList);
    }

    /**
     * 内部辅助方法，用于创建新的tag
     */
    private Tag creatNew(String tagPath) throws NewEntityException {
        // 按照/分割tag
        String[] tags = tagPath.split("/");
        // 从第一个tag开始，如果不存在，则依次创建tag
        // 使用事务进行控制，如果重名tag存在于其他层级中，则抛出异常，回滚事务
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
    @Transactional
    public Tag createNewTag(String tagPath) throws NewEntityException {
        return this.creatNew(tagPath);
    }

    @Override
    @Transactional
    public List<TagDTO> createNewTags(List<String> tagPathList) throws NewEntityException {
        List<Tag> tagList = new ArrayList<>();
        for (String tagPath : tagPathList) {
            Tag tag = this.creatNew(tagPath);
            tagList.add(tag);
        }
        return this.getTagDTOListWithAllChildren(tagList);
    }

    @Override
    public void deleteTagByName(String tagName) {
        // todo
        // 删除 Match

        // 删除 子tag

        // 删除 tag
    }
}
