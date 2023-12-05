package com.zmark.mytodo.result;

/**
 * @author ZMARK
 * @className ResultFactory
 * @description: 请求结果返回类工厂
 * @date 2023/3/5 21:45
 */
public class ResultFactory {

    public static Result buildResult(Integer resultCode, String msg, Object data) {
        return new Result(resultCode, msg, data);
    }

    public static Result buildResult(ResultCode resultCode, String msg, Object data) {
        return new Result(resultCode.getCode(), msg, data);
    }

    public static Result buildSuccessResult(Object data) {
        return buildResult(ResultCode.SUCCESS, "成功", data);
    }

    public static Result buildSuccessResult() {
        return buildResult(ResultCode.SUCCESS, "成功", null);
    }

    public static Result buildSuccessResult(String msg, Object data) {
        return buildResult(ResultCode.SUCCESS, msg, data);
    }

    public static Result buildFailResult(String msg) {
        return buildResult(ResultCode.FAIL, msg, null);
    }

    public static Result buildInternalServerErrorResult() {
        return buildResult(ResultCode.INTERNAL_SERVER_ERROR, "服务器开小差啦~", null);
    }

    public static Result buildInsufficientPermissionsResult() {
        return buildResult(ResultCode.UNAUTHORIZED, "您的权限不足！", null);
    }

    public static Result buildInsufficientPermissionsResult(String msg) {
        return buildResult(ResultCode.UNAUTHORIZED, msg, null);
    }

    public static Result buildNotFoundResult(String msg) {
        return buildResult(ResultCode.NOT_FOUND, msg, null);
    }

}
