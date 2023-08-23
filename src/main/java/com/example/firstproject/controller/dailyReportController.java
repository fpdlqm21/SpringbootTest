package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class dailyReportController {

    @GetMapping("/dailyReport/popup")
    public String popUp(){
        return "dailyReport/popUp";
    }

    @GetMapping("/dailyReport")
    public String index(){
        return "dailyReport/home";
    }
}
