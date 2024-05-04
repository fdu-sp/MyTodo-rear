package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TimerDAO;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.service.api.ITimerService;
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
        Long taskId = createReq.getTaskId();

        // 某时刻最多只能有一个计时器
        List<Timer> timers = timerDAO.findByEndTimeIsNull();
        if (!timers.isEmpty()) {
            throw new NewEntityException("创建计时器失败！某时刻最多只能有一个计时器！");
        }
        // 对应的任务必须存在
        Task task = taskDAO.findTaskById(taskId);
        if (task == null) {
            throw new NoDataInDataBaseException("Task", taskId);
        }
        // 只能对未完成的任务进行计时
        if (task.getCompleted()) {
            throw new NewEntityException("创建计时器失败！不能对已完成的任务进行计时！");
        }

        Timer timer = Timer.fromTimerCreateReq(createReq);
        timerDAO.save(timer);
        return TimerDTO.from(timer);
    }
}
