package com.zmark.mytodo.scheduler;

import com.zmark.mytodo.service.impl.MyDayTaskService;
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

    private final MyDayTaskService myDayTaskService;

    @Autowired
    public MyDayTaskScheduler(MyDayTaskService myDayTaskService) {
        this.myDayTaskService = myDayTaskService;
    }

    /**
     * 每天凌晨0点执行，清空我的一天列表
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void clearMyDayList() {
        log.info("时间到达凌晨0点，清空我的一天列表");
        myDayTaskService.clearMyDayList();
    }
}
