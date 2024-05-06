package com.zmark.mytodo.service.impl;

import com.zmark.mytodo.bo.timer.req.TimerCreateReq;
import com.zmark.mytodo.bo.timer.req.TimerUpdateReq;
import com.zmark.mytodo.dao.TaskDAO;
import com.zmark.mytodo.dao.TimerDAO;
import com.zmark.mytodo.dto.timer.TimerDTO;
import com.zmark.mytodo.dto.timer.TimerDayDTO;
import com.zmark.mytodo.dto.timer.TimerMonthDTO;
import com.zmark.mytodo.entity.Task;
import com.zmark.mytodo.entity.Timer;
import com.zmark.mytodo.exception.NewEntityException;
import com.zmark.mytodo.exception.NoDataInDataBaseException;
import com.zmark.mytodo.exception.UpdateEntityException;
import com.zmark.mytodo.service.api.ITimerService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        List<Timer> timers = timerDAO.findByEndTimestampIsNull();
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
    public TimerDTO updateTimer(TimerUpdateReq timerUpdateReq) throws NoDataInDataBaseException, UpdateEntityException {
        // 对应的计时器必须存在
        Long timerId = timerUpdateReq.getId();
        Timer timer = timerDAO.findTimerById(timerId);
        if (timer == null) {
            throw new NoDataInDataBaseException("Timer", timerId);
        }
        // 计时器的结束时间已经被设置
        if (timer.getEndTimestamp() != null) {
            throw new UpdateEntityException("该计时器已被结束！请勿重复操作！");
        }
        // 计时器结束时间晚于开始时间
        Timestamp endTimestamp = TimeUtils.toTimestamp(timerUpdateReq.getEndTimestamp());
        if (endTimestamp.before(timer.getStartTimestamp())) {
            throw new UpdateEntityException("计时器结束时间不能早于开始时间！");
        }
        timer.setEndTimestamp(endTimestamp);
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
        List<Timer> timers = timerDAO.findByEndTimestampIsNull();
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

    @Override
    public List<TimerDayDTO> getTimerWeekAnalysis() {
        // 查找近一周所有计时器
        // TODO: 目前只根据开始时间查找，没有检查结束时间可能在一周内的
        List<Timer> timers = timerDAO.findAllByStartTimestampBetweenAndEndTimestampIsNotNull(TimeUtils.getStartOfWeek(TimeUtils.now()), TimeUtils.now());
//        List<Timer> timers = timerDAO.findAllByStartTimestampBetweenAndEndTimestampIsNotNull(TimeUtils.before(7), TimeUtils.now());
        System.out.println("timers = " + timers.toString());
        // 统计每个计时器的信息（专注日期及专注时长），考虑一个计时器横框多天的情况
        List<TimerDayDTO> timerDayDTOList = new ArrayList<>();
        for (Timer timer : timers) {
            timerDayDTOList.addAll(TimerDayDTO.from(timer));
        }
        System.out.println("timerDayDTOList = " + timerDayDTOList.toString());
        return timerDayDTOList;
    }

    @Override
    public List<TimerMonthDTO> getTimerMonthAnalysis() {
        System.out.println("start time = " + TimeUtils.getStartOfMonth(TimeUtils.now()));
        List<Timer> timers = timerDAO.findAllByStartTimestampBetweenAndEndTimestampIsNotNull(TimeUtils.getStartOfMonth(TimeUtils.now()), TimeUtils.now());

        // 统计每个计时器的信息（专注日期及专注时长），考虑一个计时器横框多天的情况
        List<TimerMonthDTO> timerMonthDTOList = new ArrayList<>();
        for (Timer timer : timers) {
            Long taskListId = taskDAO.findTaskById(timer.getTaskId()).getTaskListId();
            Long focusTime = TimeUtils.minutesDiff(timer.getStartTimestamp(), timer.getEndTimestamp());
            TimerMonthDTO timerMonthDTO = TimerMonthDTO.builder()
                    .taskListId(taskListId)
                    .focusTime(focusTime)
                    .build();
            timerMonthDTOList.add(timerMonthDTO);
//            timerMonthDTOList.addAll(timerMonthDTOList.from(timer));
        }
        System.out.println("timerDayDTOList = " + timerMonthDTOList.toString());
        return timerMonthDTOList;
    }
}