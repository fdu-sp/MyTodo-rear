package com.zmark.mytodo.controller;

import com.zmark.mytodo.result.Result;
import com.zmark.mytodo.result.ResultFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Result hello() {
        return ResultFactory.buildSuccessResult("Hello World!", "Hello World!");
    }
}
