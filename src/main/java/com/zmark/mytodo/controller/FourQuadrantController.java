package com.zmark.mytodo.controller;

import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import com.zmark.mytodo.service.api.IFourQuadrantService;
import com.zmark.mytodo.service.impl.FourQuadrantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 四象限相关
 *
 * @author ZMark
 * @date 2023/12/13 3:18
 */
@Slf4j
@RestController
public class FourQuadrantController {
    private final IFourQuadrantService fourQuadrantService;

    @Autowired
    public FourQuadrantController(FourQuadrantService fourQuadrantService) {
        this.fourQuadrantService = fourQuadrantService;
    }

    @GetMapping("/api/four-quadrant/get-by-list/{task-list-id}")
    public Result getFourQuadrantDetailByList(@PathVariable("task-list-id") Long taskListId) {
        try {
            return ResultFactory.buildSuccessResult(fourQuadrantService.getFourQuadrantDetailByList(taskListId));
        } catch (NoDataInDataBaseException e) {
            log.warn("获取指定清单的 四象限 详细信息失败！清单不存在！清单id：{}", taskListId);
            return ResultFactory.buildNotFoundResult("清单不存在！");
        } catch (RuntimeException e) {
            log.error("获取指定清单的 四象限 详细信息失败！清单id：{}", taskListId, e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }

    @GetMapping("/api/four-quadrant/get-by-my-day")
    public Result getFourQuadrantDetailByMyDay() {
        try {
            return ResultFactory.buildSuccessResult(fourQuadrantService.getFourQuadrantDetailByMyDay());
        } catch (RuntimeException e) {
            log.error("获取 我的一天 的 四象限 详细信息失败！", e);
            return ResultFactory.buildInternalServerErrorResult();
        }
    }
}
