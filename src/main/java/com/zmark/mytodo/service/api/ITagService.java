package com.zmark.mytodo.service.api;

import com.zmark.mytodo.dto.tag.TagDTO;
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

    @Transactional
    void createNewTag(String tag);
}
