package com.zmark.mytodo.exception;

/**
 * @author ZMARK
 * 业务异常-数据库中没有当前数据 <br>
 * 该异常的创建示例： <br>
 * throw new NoDataInDataBaseException(String.format("id为%d的账户不存在！", srcAccountId)); <br>
 * throw new NoDataInDataBaseException("账户不存在！"); <br>
 * 该异常的捕获处理示例：（在controller层） <br>
 * catch (NoDataInDataBaseException e) { <br>
 * log.info(e.getMessage()); <br>
 * return ResultFactory.buildFailResult("您还没有账户，请先开通账户"); <br>
 * } <br>
 * catch (NoDataInDataBaseException e){ <br>
 * log.warn("请求有误：前端请求了不存在的用户, userId={}", userId); <br>
 * return ResultFactory.buildFailResult(); <br>
 * } <br>
 * @date 2023/4/12 18:38
 */
public class NoDataInDataBaseException extends Exception {

    public NoDataInDataBaseException(String entityClassName, long id) {
        super(String.format("id为%d的%s不存在", id, entityClassName));
    }

    public NoDataInDataBaseException(String msg) {
        super(msg);
    }

    public NoDataInDataBaseException(Exception e) {
        super(e);
    }

    public NoDataInDataBaseException(String entityClassName, String name) {
        super(String.format("名称为%s的%s不存在", name, entityClassName));
    }
}
