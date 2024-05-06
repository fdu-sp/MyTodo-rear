package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.bo.timer.req.TimerUpdateReq;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.dto.timer.TimerDayDTO;
import com.zmark.mytodo.dto.timer.TimerMonthDTO;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.UpdateEntityException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Violette
 * @date 2024/5/4 15:17
 */
public interface ITimerService {

    /**
     * 如果创建成功，返回新创建的计时器DTO
     *
     * @param timerCreateReq 创建计时器的请求
     * @throws NewEntityException        如果创建失败，抛出异常（当前已有计时器存在，或者对已完成的任务创建计时器）
     * @throws NoDataInDataBaseException 如果关联的任务不存在，抛出异常
     */
    @Transactional
    TimerDTO createNewTimer(TimerCreateReq timerCreateReq) throws NewEntityException, NoDataInDataBaseException;

    /**
     * 如果更新成功，返回对应计时器DTO
     *
     * @param timerUpdateReq 更新计时器的请求
     * @throws NoDataInDataBaseException 如果对应的计时器或关联的任务不存在，抛出异常
     * @throws UpdateEntityException     如果计时器的结束时间已经被设置，或是将要设置的结束时间早于开始时间，抛出异常
     */
    @Transactional
    TimerDTO updateTimer(TimerUpdateReq timerUpdateReq) throws NoDataInDataBaseException, UpdateEntityException;

    /**
     * 获取当前正在计时的计时器
     *
     * @return 当前正在计时的计时器，若没有则将其中字段设为null（默认）即可
     * @throws RuntimeException 如果后台存在多个正在计时的计时器，抛出异常
     */
    TimerDTO getCurrentTimer() throws RuntimeException;

    /**
     * 获取本周专注时长信息
     *
     * @return 专注时长信息列表，包含日期及当日专注时间
     */
    List<TimerDayDTO> getTimerWeekAnalysis();

    /**
     * 获取本月各清单专注信息
     *
     * @return 专注时长信息列表，包含清单id及当月该清单专注时间
     */
    List<TimerMonthDTO> getTimerMonthAnalysis();
}
