package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.bo.timer.req.TimerUpdateReq;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TimerDAO;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.RepeatedEntityInDatabase;
import com.zmark.mytodo.service.api.ITimerService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Violette
 * @date 2024/5/4 16:20
 */
@Slf4j
@Service
public class TimerService implements ITimerService {
    private final TimerDAO timerDAO;
    private final TaskDAO taskDAO;

    @Autowired
    public TimerService(TimerDAO timerDAO, TaskDAO taskDAO) {
        this.timerDAO = timerDAO;
        this.taskDAO = taskDAO;
    }

    @Override
    public TimerDTO createNewTimer(TimerCreateReq createReq) throws NewEntityException, NoDataInDataBaseException {
        // 某时刻最多只能有一个计时器
        List<Timer> timers = timerDAO.findByEndTimeIsNull();
        if (!timers.isEmpty()) {
            throw new NewEntityException("创建计时器失败！某时刻最多只能有一个计时器！");
        }
        // 对应的任务必须存在
        Long taskId = createReq.getTaskId();
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("Task", taskId);
        }
        // 只能对未完成的任务进行计时
        if (task.getCompleted()) {
            throw new NewEntityException("创建计时器失败！该任务已完成");
        }

        Timer timer = Timer.fromTimerCreateReq(createReq);
        timerDAO.save(timer);
        return TimerDTO.from(timer);
    }

    @Override
    public TimerDTO updateTimer(TimerUpdateReq timerUpdateReq) throws NoDataInDataBaseException, RepeatedEntityInDatabase {
        // 对应的计时器必须存在
        Long timerId = timerUpdateReq.getId();
        Timer timer = timerDAO.findTimerById(timerId);
        if (timer == null) {
            throw new NoDataInDataBaseException("Timer", timerId);
        }
        // 计时器的结束时间已经被设置
        if (timer.getEndTime() != null) {
            throw new RepeatedEntityInDatabase("该计时器已被结束！请勿重复操作！");
        }
        // 设置计时器结束时间
        timer.setEndTime(TimeUtils.toTimestamp(timerUpdateReq.getEndTime()));
        // 检查该任务在本次计时期间是否被完成，若完成则更新计时器完成状态
        Long taskId = timer.getTaskId();
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("Task", taskId);
        }
        if (task.getCompleted()) {
            timer.setCompleted(true);
        }

        timerDAO.save(timer);
        return TimerDTO.from(timer);
    }

    @Override
    public TimerDTO getCurrentTimer() throws RuntimeException {
        List<Timer> timers = timerDAO.findByEndTimeIsNull();
        if (timers.isEmpty()) {
            // 当前不存在正在计时的计时器
            Timer timer = Timer.builder().build();
            log.info("null timer = {}", timer);
            return TimerDTO.from(timer);
        } else if (timers.size() > 1) {
            // 有多于1个正在计时的计时器
            // 新建计时器时会检查，出现多个正在计时的计时器有可能是由于并发或其他不可控因素导致的
            throw new RuntimeException("后台错误！有多个正在计时的计时器！");
        } else {
            // 有1个正在计时的计时器
            Timer timer = timers.get(0);
            return TimerDTO.from(timer);
        }
    }
}
