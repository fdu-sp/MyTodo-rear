package com.zmark.mytodo.controller;

import com.zmark.mytodo.dto.tag.TagDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.ITagService;
import com.zmark.mytodo.service.impl.TagService;
import com.zmark.mytodo.vo.tag.req.TagCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 20:43
 */
@Slf4j
@Validated
@RestController
public class TagController {
    private final ITagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/api/tag/create-new-tags")
    public Result createNewTasks(@Validated @RequestBody TagCreateReq tagCreateReq) {
        try {
            List<TagDTO> tagList = tagService.createNewTags(tagCreateReq.getTagPathList());
            return ResultFactory.buildSuccessResult(TagDTO.toDetailResp(tagList));
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
            return ResultFactory.buildSuccessResult(TagDTO.toDetailResp(tagDTOList));
        } catch (Exception e) {
            log.error("getAllFirstLevelTags error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/details/get-all-tags")
    public Result getAllTags() {
        try {
            List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDTO.toDetailResp(tagDTOList));
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
            return ResultFactory.buildSuccessResult(TagDTO.toDetailResp(tagDTO));
        } catch (Exception e) {
            log.error("getTagByName error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/simple/get-all-first-level-tags")
    public Result getAllFirstLevelTagsSimple() {
        try {
            List<TagDTO> tagDTOList = tagService.findFirstLevelTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDTO.toSimpleResp(tagDTOList));
        } catch (Exception e) {
            log.error("getAllFirstLevelTagsSimple error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/tag/simple/get-all-tags")
    public Result getAllTagsSimple() {
        try {
            List<TagDTO> tagDTOList = tagService.findAllTagsWithAllChildren();
            return ResultFactory.buildSuccessResult(TagDTO.toSimpleResp(tagDTOList));
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
            return ResultFactory.buildSuccessResult(TagDTO.toSimpleResp(tagDTO));
        } catch (Exception e) {
            log.error("getTagByNameSimple error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    /**
     * TODO: 2023/12/5
     *
     * @see com.zmark.mytodo.service.api.ITagService#deleteTagByName(String) 未完成
     */
    @DeleteMapping("/api/tag/delete-tag-by-name/{tag-name}")
    public Result deleteTagByName(@PathVariable("tag-name") String tagName) {
        try {
            tagService.deleteTagByName(tagName);
            return ResultFactory.buildSuccessResult();
        } catch (Exception e) {
            log.error("deleteTagByName error:" + e.getMessage(), e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
