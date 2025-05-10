package com.springboot.homework.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hello(HttpServletRequest request){
        return "Hello, Spring: " + request.getSession().getId();
    }
}
