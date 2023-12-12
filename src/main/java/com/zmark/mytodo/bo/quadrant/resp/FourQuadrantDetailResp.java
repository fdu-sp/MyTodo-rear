package com.zmark.mytodo.bo.quadrant.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMark
 * @date 2023/12/13 3:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FourQuadrantDetailResp {
    private OneQuadrantDetailResp urgentAndImportant;
    private OneQuadrantDetailResp urgentAndNotImportant;
    private OneQuadrantDetailResp notUrgentAndImportant;
    private OneQuadrantDetailResp notUrgentAndNotImportant;
}
