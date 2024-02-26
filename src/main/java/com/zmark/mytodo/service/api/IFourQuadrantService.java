package com.zmark.mytodo.service.api;

import com.zmark.mytodo.bo.quadrant.resp.FourQuadrantDetailResp;
import com.zmark.mytodo.exception.NoDataInDataBaseException;

/**
 * @author ZMark
 * @date 2023/12/13 3:19
 */
public interface IFourQuadrantService {

    /**
     * 获取指定清单的 四象限 详细信息
     *
     * @param taskListId 清单id
     * @throws NoDataInDataBaseException 如果清单不存在
     */
    FourQuadrantDetailResp getFourQuadrantDetailByList(Long taskListId) throws NoDataInDataBaseException;

    /**
     * 获取 我的一天 的 四象限 详细信息
     */
    FourQuadrantDetailResp getFourQuadrantDetailByMyDay();
}
