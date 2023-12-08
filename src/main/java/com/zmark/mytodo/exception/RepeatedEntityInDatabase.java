package com.zmark.mytodo.exception;

/**
 * @author ZMark
 * @date 2023/12/8 14:29
 */
public class RepeatedEntityInDatabase extends Exception {
    public RepeatedEntityInDatabase(String message) {
        super(message);
    }

    public static RepeatedEntityInDatabase RepeatEntityName(String entityClassName, String name) {
        return new RepeatedEntityInDatabase(String.format("已存在name为%s的%s", name, entityClassName));
    }
}
