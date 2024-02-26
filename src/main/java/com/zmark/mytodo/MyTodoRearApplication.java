package com.zmark.mytodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyTodoRearApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTodoRearApplication.class, args);
    }

}
