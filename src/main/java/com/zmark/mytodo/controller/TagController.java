package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.entity.Tag;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.impl.TagService;
import com.zmark.mytodo.vo.tag.req.TagCreateReq;
import com.zmark.mytodo.vo.tag.resp.TagDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 20:43
 */
@RestController
public class TagController {
    private final ITagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/api/tag/create-new-tag")
    public Result createNewTask(@RequestBody TagCreateReq tagCreateReq) {
        try {
            Tag tag = tagService.createNewTag(tagCreateReq.getTagPath());
            return ResultFactory.buildSuccessResult(TagDetailResp.from(TagDTO.from(tag)));
        } catch (NewEntityException e) {
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/details/get-all-first-level-tags")
    public Result getAllFirstLevelTags() {
        try {
            List<TagDTO> tagDTOList = tagService.findFirstLevelTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDetailResp.from(tagDTOList));
        } catch (Exception e) {
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
