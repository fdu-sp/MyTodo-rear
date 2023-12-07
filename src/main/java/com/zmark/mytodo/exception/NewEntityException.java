package com.zmark.mytodo.exception;

/**
 * 数据中新增数据时，对外展示的业务异常<br/>
 * 该异常的创建示例：捕获到DAO.save的异常<br/>
 * catch (Exception e) {<br/>
 * log.error("创建账户失败！",e);<br/>
 * throw new NewEntityException(e);<br/>
 * }<br/>
 * 该异常的捕获处理示例：<br/>
 * catch (NewEntityException e) {<br/>
 * return ResultFactory.buildInternalServerErrorResult();<br/>
 * }<br/>
 *
 * @author ZMARK
 * @date 2023/3/19 16:12
 */
public class NewEntityException extends Exception {

    public NewEntityException(String entityClassName, String name) {
        super(String.format("创建%s失败！%s", entityClassName, name));
    }

    public NewEntityException() {
        super();
    }

    public NewEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewEntityException(String message) {
        super(message);
    }

    public NewEntityException(Exception e) {
        super(e);
    }


    public static NewEntityException RepeatEntityName(String entityClassName, String name) {
        return new NewEntityException(String.format("已存在name为%s的%s", name, entityClassName));
    }
}

