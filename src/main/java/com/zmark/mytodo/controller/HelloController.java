package com.zmark.mytodo.controller;

import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Result hello() {
        log.debug("Hello World! api called!");
        return ResultFactory.buildSuccessResult("Hello World!", "Hello ZMark!");
    }
}
