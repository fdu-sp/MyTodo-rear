package com.zmark.mytodo.exception;

/**
 * @author ZMARK
 * @className RegisterException
 * @description: 数据中新增数据时，对外展示的业务异常
 * 该异常的创建示例：捕获到DAO.save的异常
 * catch (Exception e) {
 * log.error("创建账户失败！",e);
 * throw new NewEntityException(e);
 * }
 * 该异常的捕获处理示例：
 * catch (NewEntityException e) {
 * return ResultFactory.buildInternalServerErrorResult();
 * }
 * @date 2023/3/19 16:12
 */
public class NewEntityException extends RuntimeException {
    public NewEntityException() {
        super();
    }

    public NewEntityException(String message) {
        super(message);
    }

    public NewEntityException(Exception e) {
        super(e);
    }
}
