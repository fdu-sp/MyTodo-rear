package com.zmark.mytodo.scheduler;

import com.zmark.mytodo.service.api.IMyDayTaskService;
import com.zmark.mytodo.service.impl.MyDayTaskService;
import com.zmark.mytodo.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * `我的一天` 相关的定时器
 *
 * @author ZMark
 * @date 2024/2/26 19:55
 */
@Slf4j
@Component
public class MyDayTaskScheduler {

    private final IMyDayTaskService myDayTaskService;

    @Autowired
    public MyDayTaskScheduler(MyDayTaskService myDayTaskService) {
        this.myDayTaskService = myDayTaskService;
    }

    /**
     * 每天凌晨0点执行：更新我的一天列表<br/>
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateMyDayTaskList() {
        log.info("时间到达凌晨0点，清空我的一天列表");
        myDayTaskService.clearMyDayList();
        log.info("目前时间为：{}，准备更新我的一天列表", TimeUtils.today());
        int taskAdded = myDayTaskService.updateMyDayTaskList();
        log.info("加入{}个今日任务到我的一天中", taskAdded);
    }
}
