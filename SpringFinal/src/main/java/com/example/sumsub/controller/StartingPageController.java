package com.example.sumsub.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/api")
public class StartingPageController {

    @GetMapping(value = "/start")
    public String hello() {
        return "start";
    }
}
