package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Violette
 * @date 2024/5/4 15:17
 */
public interface ITimerService {

    /**
     * 如果创建成功，返回新创建的计时器DTO
     *
     * @param timerCreateReq 创建计时器的请求
     * @throws NewEntityException 如果创建失败，抛出异常（当前已有计时器存在，或者对已完成的任务创建计时器）
     * @throws NoDataInDataBaseException 如果关联的任务不存在，抛出异常
     */
    @Transactional
    TimerDTO createNewTimer(TimerCreateReq timerCreateReq) throws NewEntityException, NoDataInDataBaseException;
}
