package com.zmark.mytodo.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZMARK
 * @className Result
 * @description: 请求结果返回类
 * @date 2023/3/5 21:38
 */
@Data
@NoArgsConstructor
public class Result {
    private int code;
    private String msg;
    private Object object;

    public Result(int code, String msg, Object object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }
}
