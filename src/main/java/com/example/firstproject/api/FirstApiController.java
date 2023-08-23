package com.example.firstproject.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RestApi 컨트롤러 JSON을 반환!, 일반 Controller는 View페이지를 반환!
public class FirstApiController {

    @GetMapping("/api/hello")
    public String hello(){
        return "Hello World!";
    }
}
