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
import com.zmark.mytodo.vo.tag.resp.TagSimpleResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 20:43
 */
@Slf4j
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
            log.error("createNewTask error:" + e.getMessage(), e);
            return ResultFactory.buildFailResult(e.getMessage());
        } catch (RuntimeException e) {
            log.error("createNewTask error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/details/get-all-first-level-tags")
    public Result getAllFirstLevelTags() {
        try {
            List<TagDTO> tagDTOList = tagService.findFirstLevelTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDetailResp.from(tagDTOList));
        } catch (Exception e) {
            log.error("getAllFirstLevelTags error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/details/get-all-tags")
    public Result getAllTags() {
        try {
            List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDetailResp.from(tagDTOList));
        } catch (Exception e) {
            log.error("getAllTags error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/details/get-tag-by-name/{tag-name}")
    public Result getTagByName(@PathVariable("tag-name") String tagName) {
        try {
            TagDTO tagDTO = tagService.findTagWithAllChildren(tagName);
            if (tagDTO == null) {
                return ResultFactory.buildFailResult("找不到tag [" + tagName + "]");
            }
            return ResultFactory.buildSuccessResult(TagDetailResp.from(tagDTO));
        } catch (Exception e) {
            log.error("getTagByName error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/simple/get-all-first-level-tags")
    public Result getAllFirstLevelTagsSimple() {
        try {
            List<TagDTO> tagDTOList = tagService.findFirstLevelTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagSimpleResp.fromTagDTO(tagDTOList));
        } catch (Exception e) {
            log.error("getAllFirstLevelTagsSimple error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/simple/get-all-tags")
    public Result getAllTagsSimple() {
        try {
            List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagSimpleResp.fromTagDTO(tagDTOList));
        } catch (Exception e) {
            log.error("getAllTagsSimple error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/simple/get-tag-by-name/{tag-name}")
    public Result getTagByNameSimple(@PathVariable("tag-name") String tagName) {
        try {
            TagDTO tagDTO = tagService.findTagWithAllChildren(tagName);
            if (tagDTO == null) {
                return ResultFactory.buildFailResult("找不到tag [" + tagName + "]");
            }
            return ResultFactory.buildSuccessResult(TagSimpleResp.from(tagDTO));
        } catch (Exception e) {
            log.error("getTagByNameSimple error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
