package com.zmark.mytodo.annotation.paramvalidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZMARK
 * @className ParamValidation
 * @description: 处理参数校验的注解
 * @date 2023/5/14 12:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamValidation {
}
