package com.zmark.mytodo.annotation.paramvalidation;

import com.zmark.mytodo.result.ResultFactory;
import jakarta.validation.Validator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

/**
 * @author ZMARK
 * @className ParamValidationAspect
 * @description: 切面类，用于拦截带有@ParamValidation注解的方法，并实现参数校验逻辑
 * @date 2023/5/14 12:55
 */
@Component
@Aspect
public class ParamValidationAspect {
    private final Validator validator;

    @Autowired
    public ParamValidationAspect(Validator validator) {
        this.validator = validator;
    }

    @Around("@annotation(com.zmark.mytodo.annotation.paramvalidation.ParamValidation)")
    public Object validateParam(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                    //getFieldErrors()方法返回了所有校验失败的字段的错误列表，
                    // 每个错误包含了一个getDefaultMessage()方法，用于获取@NotNull注解中定义的message信息
                    //使用了Java 8 Stream API对错误列表进行了处理，
                    // 通过map()方法将每个错误转换为其对应的message信息，
                    // 最终通过Collectors.joining()方法将所有message信息拼接成一个字符串，以";"分隔每个错误信息
                    //最终获得的errorMessage变量将包含所有校验失败的错误信息，以";"分隔
                    String errorMessage = bindingResult.getFieldErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("; "));
                    return ResultFactory.buildFailResult(errorMessage);
                }
            }
        }
        return joinPoint.proceed();
    }
}
