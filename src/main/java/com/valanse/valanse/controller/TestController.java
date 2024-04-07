package com.valanse.valanse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/success")
    public String successTest() {
        return "success";
    }

    @GetMapping("/failure")
    public String failureTest() {
        return "failure";
    }
}