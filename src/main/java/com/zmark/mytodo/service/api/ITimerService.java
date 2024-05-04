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
    @Transactional
    TimerDTO createNewTimer(TimerCreateReq createReq) throws NewEntityException, NoDataInDataBaseException;
}
