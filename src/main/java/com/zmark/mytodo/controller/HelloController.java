package com.zmark.mytodo.controller;

import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class HelloController {

    public static final List<String> welcomeMsgList;


    static {
        welcomeMsgList = List.of("Hello!", "Welcome!", "Nice to meet you!", "Good day!");
    }

    @GetMapping("/api/hello")
    public Result hello() {
        log.debug("Hello World! api called!");
        // todo 实现用户管理后，需要返回当前用户的信息
        // 随机返回欢迎信息
        int index = (int) (Math.random() * welcomeMsgList.size());
        // 返回结果
        return ResultFactory.buildSuccessResult("Hello World!", welcomeMsgList.get(index));
    }
}
