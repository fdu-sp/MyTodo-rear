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
    /**
     * 表示Result是否按照协议进行
     */
    private boolean uxApi;
    /**
     * 请求是否成功
     */
    private boolean success;
    private String msg;
    private Object object;

    public Result(int code, boolean success, String msg, Object object) {
        this.code = code;
        this.uxApi = true;
        this.success = success;
        this.msg = msg;
        this.object = object;
    }
}
